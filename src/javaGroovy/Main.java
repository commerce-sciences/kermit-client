package javaGroovy;

public class Main {

    public static void main(String[] args) {
        GroovyLoader.createGroovyInstance(IGroovyChallenge.class).saveKermit();
    }
}
