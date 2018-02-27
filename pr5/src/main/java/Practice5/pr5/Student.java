package Practice5.pr5;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Student{
	
@Id Long studentId;

String name;
Integer course;

Student(String name, Integer course){
	this.name = name;
	this.course = course;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Integer getCourse() {
	return course;
}

public void setCourse(Integer course) {
	this.course = course;
}

}