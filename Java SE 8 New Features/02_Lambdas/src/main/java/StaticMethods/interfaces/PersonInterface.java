package StaticMethods.interfaces;

import StaticMethods.model.Person;

public interface PersonInterface {

    String getName();

    void setName(String name);

    int getAge();

    void setAge(int age);

    static String getPersonInfo(Person p) {
        return p.getName() + "( " + p.getAge() + ")";
    }

}
