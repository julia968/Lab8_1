package Classes;

import gui.Launcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;


public class Person {
    private static Integer idCounter = 1 ;

    private Integer id;

    private String name;

    private Coordinates coordinates;

    private LocalDate creationDate;

    private long height;

    private Date birthday;

    private EyeColor eyeColor;

    private Country nationality;

    private Location location;
    private String creator;


    public Person() {
       this.creationDate = LocalDate.now(); 
   }


    public Person(Integer id, String name, Coordinates coordinates, LocalDate creationDate, long height, Date birthday, EyeColor eyeColor, Country nationality, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.height = height;
        this.birthday = birthday;
        this.eyeColor = eyeColor;
        this.nationality = nationality;
        this.location = location;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setId() {
        this.id = idCounter++;
        }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        if (name == null || " ".equals(name)) {
            throw new IllegalArgumentException("Человек не может быть без имени, попробуйте еще раз");
        } else {
            this.name = name;
        }
    }

    public String getName() {
        return name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть пустыми, попробуйте еще раз");
        } else {
            this.coordinates = coordinates;
        }
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setHeight(long height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Рост не может быть отрицательным, попробуййте снова");
        } else {
            this.height = height;
        }
    }

    public long getHeight() {
        return height;
    }

    public void setEyeColor(EyeColor eyeColor) {
        if (eyeColor == null) {
            throw new IllegalArgumentException("Глаза не могут быть бесцветными");
        } else {
            this.eyeColor = eyeColor;
        }
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setNationality(Country nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Человек не может быть без национальности");
        } else {
            this.nationality = nationality;
        }
    }

    public Country getCountry() {
        return nationality;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public Location getLocation() {
        return location;
    }

    public Date getBirthday() {
        return birthday;
    }


    public void setBirthday(Date birthday) {this.birthday = birthday; }


    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Person update(Person person){
        setName(person.getName());
        setCoordinates(person.getCoordinates());
        setHeight(person.getHeight());
        setEyeColor(person.getEyeColor());
        setNationality(person.getCountry());
        setLocation(person.getLocation());
        setCreationDate(person.getCreationDate());
        return this;
    }

    public static class SortByname implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Person personOne = (Person) o1;
            Person persomTwo = (Person) o2;
            return personOne.getName().compareTo(persomTwo.getName());
        }
    }

    @Override
    public String toString() {
        return Launcher.getAppLanguage().getString("barbie") + id + "\n" +
                Launcher.getAppLanguage().getString("name") + name + "\n" +
                Launcher.getAppLanguage().getString("height") + height + "\n" +
                Launcher.getAppLanguage().getString("coords") + coordinates + "\n" +
                Launcher.getAppLanguage().getString("creation_date") + new SimpleDateFormat(Launcher.getAppLanguage().getString("date_format")).format(Date.from(creationDate.atStartOfDay(ZoneId.systemDefault()).toInstant())) + "\n" +
                Launcher.getAppLanguage().getString("eyes_color") + eyeColor + "\n" +
                Launcher.getAppLanguage().getString("bd") + new SimpleDateFormat(Launcher.getAppLanguage().getString("date_format")).format(birthday) + "\n" +
                Launcher.getAppLanguage().getString("country") + nationality + "\n" +
                Launcher.getAppLanguage().getString("location") + location + "\n" +
                Launcher.getAppLanguage().getString("creator") + creator;
    }
}
