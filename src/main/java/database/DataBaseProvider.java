package database;

import Classes.*;
import services.CurrentUserManager;
import sql.SQLConnection;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import static Utils.Parser.convertTimeStampToLocalDate;
import static Utils.Parser.parseLocalDateToTimestamp;

public class DataBaseProvider {
    private final SQLConnection sqlConnection;
    private final LocalDateTime creationDate;
    private final String pepper = "*61&^dQLC(#";
    private CurrentUserManager userManager;
    private Set<Person> dataSet;

    public DataBaseProvider(CurrentUserManager userManager) {
        this.sqlConnection = new SQLConnection();
        this.userManager = userManager;
        this.dataSet = loadDataBase();
        this.creationDate = LocalDateTime.now();
    }

    private static String saltBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            stringBuilder.append((char) new Random().nextInt(33, 126));
        }
        return stringBuilder.toString();
    }

    public static String md2encoding(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addPersonToDataBase(Person person){
        int id = -1;
        try {
            String query = "INSERT INTO persons VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING person_id";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, addCoordinatesToDB(person.getCoordinates()));
            preparedStatement.setLong(3, person.getHeight());
            preparedStatement.setString(4, person.getEyeColor().toString());
            preparedStatement.setString(5, person.getCountry().toString());
            preparedStatement.setInt(6, addLocationToDB(person.getLocation()));
            preparedStatement.setTimestamp(7, parseLocalDateToTimestamp(person.getCreationDate()));
            preparedStatement.setDate(8, new java.sql.Date(person.getBirthday().getTime()));
            preparedStatement.setString(9, userManager.getUserName());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("person_id");
            }

            preparedStatement.close();

            person.setId(id);
            person.setCreator(userManager.getUserName());

            dataSet.add(person);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id > 0;
    }

    public boolean updatePersonInDataBase(Person person, int id){
        try {
            String query = "UPDATE persons SET name = ?, coordinates_id = ?, height = ?, " +
                    "eye_color = ?, country = ?, location_id = ?, creation_time = ?, birthday_date = ? " +
                    "WHERE person_id = ?";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, addCoordinatesToDB(person.getCoordinates()));
            preparedStatement.setLong(3, person.getHeight());
            preparedStatement.setString(4, person.getEyeColor().toString());
            preparedStatement.setString(5, person.getCountry().toString());
            preparedStatement.setInt(6, addLocationToDB(person.getLocation()));
            preparedStatement.setTimestamp(7, parseLocalDateToTimestamp(person.getCreationDate()));
            preparedStatement.setDate(8, new java.sql.Date(person.getBirthday().getTime()));
            preparedStatement.setInt(9, id);

            int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (affectedRows != 0){
                dataSet.stream().filter(p -> p.getId().equals(id)).toList().get(0).update(person);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person getPersonById(int id){
        Person response = new Person();
        if (findPersonById(id)) {
            try {
                String query = "SELECT * FROM persons WHERE person_id = ?";
                PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    response.setId(id);
                    response.setName(resultSet.getString("name"));
                    response.setCoordinates(getCoordinates(resultSet.getInt("coordinates_id")));
                    response.setCreationDate(convertTimeStampToLocalDate(resultSet.getTimestamp("creation_time")));
                    response.setHeight(resultSet.getLong("height"));
                    response.setBirthday(resultSet.getDate("birthday_date"));
                    response.setEyeColor(EyeColor.valueOf(resultSet.getString("eye_color")));
                    response.setNationality(Country.valueOf(resultSet.getString("country")));
                    response.setLocation(getLocation(resultSet.getInt("location_id")));
                    response.setCreator(resultSet.getString("creator_name"));
                }
                preparedStatement.close();

                return response;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public boolean findPersonById(int id) {
        Set<Integer> idSet = new HashSet<>();
        try {
            String query = "SELECT person_id FROM persons";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                idSet.add(resultSet.getInt("person_id"));
            }
            if (idSet.contains(id)) {
                return true;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public boolean removePersonById(int id){
        if (!findPersonById(id)){
            return false;
        } else {
            try {
                String query = "DELETE FROM persons WHERE creator_name = ? AND person_id = ?";
                PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
                preparedStatement.setString(1, userManager.getUserName());
                preparedStatement.setInt(2, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows != 0) {
                    dataSet.removeIf(p-> p.getCreator().equals(userManager.getUserName()));
                    return true;
                }
                preparedStatement.close();
                return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Coordinates getCoordinates(int id) {
        Coordinates coordinates = new Coordinates();
        try {
            String query = "SELECT coordinate_x, coordinate_y FROM coordinates WHERE coordinates_id = ?";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                coordinates.setX(resultSet.getLong("coordinate_x"));
                coordinates.setY(resultSet.getInt("coordinate_y"));
            }
            preparedStatement.close();
            return coordinates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int addCoordinatesToDB(Coordinates coordinates) {
        int coordinatesId = 0;
        try {
            String query = "INSERT INTO coordinates VALUES (DEFAULT, ?, ?) RETURNING coordinates_id";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, coordinates.getX());
            preparedStatement.setDouble(2, coordinates.getY());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                coordinatesId = resultSet.getInt("coordinates_id");
            }
            preparedStatement.close();
            return coordinatesId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Location getLocation(int id) {
        Location location = new Location();
        try {
            String query = "SELECT location_x, location_y FROM location WHERE location_id = ?";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                location.setX(resultSet.getLong("location_x"));
                location.setY(resultSet.getFloat("location_y"));
            }
            preparedStatement.close();
            return location;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int addLocationToDB(Location location) {
        int locationId = 0;
        try {
            String query = "INSERT INTO location VALUES (DEFAULT, ?, ?) RETURNING location_id";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, location.getX());
            preparedStatement.setDouble(2, location.getY());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                locationId = resultSet.getInt("location_id");
            }
            preparedStatement.close();
            return locationId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Person> updateDataSet() {
        dataSet = loadDataBase();
        return dataSet;
    }

    private Set<Person> loadDataBase(){
        Set<Person> dbSet = new LinkedHashSet<>();
        try {
            String query = "SELECT * FROM persons";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Person response = new Person();
                response.setId(resultSet.getInt("person_id"));
                response.setName(resultSet.getString("name"));
                response.setCoordinates(getCoordinates(resultSet.getInt("coordinates_id")));
                response.setCreationDate(convertTimeStampToLocalDate(resultSet.getTimestamp("creation_time")));
                response.setHeight(resultSet.getLong("height"));
                response.setBirthday(resultSet.getDate("birthday_date"));
                response.setEyeColor(EyeColor.valueOf(resultSet.getString("eye_color")));
                response.setNationality(Country.valueOf(resultSet.getString("country")));
                response.setLocation(getLocation(resultSet.getInt("location_id")));
                response.setCreator(resultSet.getString("creator_name"));
                dbSet.add(getPersonById(resultSet.getInt("person_id")));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dbSet;
    }

    private String getPassword(String userName) {
        String pass = "";
        try {
            String query = "SELECT password FROM users WHERE user_name = ?";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pass = (resultSet.getString("password"));
            }
            preparedStatement.close();
            return pass;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void userRegister(String username, String password) {
        try {
            String salt = saltBuilder();
            String query = "INSERT INTO users VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, md2encoding(pepper + password + salt));
            preparedStatement.setString(3, salt);


            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Ошибка! Ничего не изменилось.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String getSalt(String userName) {
        String salt = "";
        try {
            String query = "SELECT salt FROM users WHERE user_name = ?";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                salt = (resultSet.getString("salt"));
            }
            preparedStatement.close();
            return salt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkUserPassword(String username, String password) {
        String passwordHash = md2encoding(pepper + password + getSalt(username));
        assert passwordHash != null;
        return passwordHash.equals(getPassword(username));
    }
    public Set<String> getUserNameSet() {
        Set<String> userList = new HashSet<>();
        try {
            String query = "SELECT user_name FROM users";
            PreparedStatement preparedStatement = sqlConnection.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(resultSet.getString("user_name"));
            }
            preparedStatement.close();
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String infoOfCollection() {
        return ("Тип коллекции: " + dataSet.getClass()
                + " дата инициализации: " + creationDate
                + " количество элементов: " + dataSet.size());
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public CurrentUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(CurrentUserManager userManager) {
        this.userManager = userManager;
    }

    public Set<Person> getDataSet() {
        return dataSet;
    }
}
