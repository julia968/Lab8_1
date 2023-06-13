package controller;

import Classes.Person;
import database.DataBaseProvider;
import services.CurrentUserManager;

import java.util.*;

public class PersonControllerImpl {
    private CurrentUserManager userManager;
    private final DataBaseProvider source;

    public PersonControllerImpl(CurrentUserManager userManager) {
        this.userManager = userManager;
        this.source = new DataBaseProvider(userManager);
    }
    public List<Person> getPersonList(){
        return source.getDataSet().stream().toList();
    }
    public String info(){
        return source.infoOfCollection();
    }
    public boolean addPersonToCollection(Person person){
        return source.addPersonToDataBase(person);
    }
    public boolean updatePerson(Person person, int id){
        return source.updatePersonInDataBase(person, id);
    }

    public void exit(){
        System.exit(0);
    }

    public boolean removeById(int id){
        return source.removePersonById(id);
    }
    public int removeGreater(int height){
        int size = source.getDataSet().size();
        source.getDataSet().stream().filter((p -> p.getHeight() > height)).toList().forEach(p->source.removePersonById(p.getId()));
        return size - source.getDataSet().size();
    }
    public int removeLower(int height){
        int size = source.getDataSet().size();
        source.getDataSet().stream().filter((p -> p.getHeight() < height)).toList().forEach(p->source.removePersonById(p.getId()));
        return size - source.getDataSet().size();
    }
    public int clear(){
        int size = source.getDataSet().size();
        source.getDataSet().stream().toList().stream()
                .filter(p -> p.getCreator().equals(source.getUserManager().getUserName()))
                .forEach(p->source.removePersonById(p.getId()));
        return size - source.getDataSet().size();
    }

    public String maxByName(){
        return source.getDataSet().stream().map(Person::getName).max(Comparator.comparingInt(String::length)).get();
    }

    public CurrentUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(CurrentUserManager userManager) {
        this.userManager = userManager;
    }

    public DataBaseProvider getSource() {
        return source;
    }

    public boolean checkUserPassword(String username, String text) {
        return source.checkUserPassword(username, text);
    }
    public Set<String> getUserNameSet(){
        return source.getUserNameSet();
    }
    public void userRegister(String username, String password){
        source.userRegister(username, password);
    }
}
