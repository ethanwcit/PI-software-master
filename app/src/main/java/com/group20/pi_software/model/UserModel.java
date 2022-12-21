package com.group20.pi_software.model;

public class UserModel
{
    private int id = 1;
    private String firstName;
    private String email;
    private String dateJoined;
    private String dateOfBirth;
    private String profilePicture;
    private double weight;
    private double height;
    private int stepsGoal;
    private int exerciseGoal;
    private double studyHoursGoal;
    private double sleepHoursGoal;

    public UserModel(String firstName, String email, String dateJoined, String dateOfBirth, String profilePicture, double weight, double height, int stepsGoal, int exerciseGoal, double studyHoursGoal, double sleepHoursGoal) {
        this.firstName = firstName;
        this.email = email;
        this.dateJoined = dateJoined;
        this.dateOfBirth = dateOfBirth;
        this.profilePicture = profilePicture;
        this.weight = weight;
        this.height = height;
        this.stepsGoal = stepsGoal;
        this.exerciseGoal = exerciseGoal;
        this.studyHoursGoal = studyHoursGoal;
        this.sleepHoursGoal = sleepHoursGoal;
    }

    public UserModel() {
    }

    // toString method is necessary for printing the contents of the object
    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + email + '\'' +
                ", dateJoined='" + dateJoined + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", stepsGoal=" + stepsGoal +
                ", exerciseGoal=" + exerciseGoal +
                ", studyHoursGoal=" + studyHoursGoal +
                ", sleepHoursGoal=" + sleepHoursGoal +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStepsGoal() {
        return stepsGoal;
    }

    public void setStepsGoal(int stepsGoal) {
        this.stepsGoal = stepsGoal;
    }

    public int getExerciseGoal() {
        return exerciseGoal;
    }

    public void setExerciseGoal(int exerciseGoal) {
        this.exerciseGoal = exerciseGoal;
    }

    public double getStudyHoursGoal() {
        return studyHoursGoal;
    }

    public void setStudyHoursGoal(double studyHoursGoal) {
        this.studyHoursGoal = studyHoursGoal;
    }

    public double getSleepHoursGoal() {
        return sleepHoursGoal;
    }

    public void setSleepHoursGoal(double sleepHoursGoal) {
        this.sleepHoursGoal = sleepHoursGoal;
    }
}
