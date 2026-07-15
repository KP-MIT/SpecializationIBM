package inheritanceExample;

// Step 1: Create an abstract class Instrument
// This should include:
// - private String name
// - protected int year (year of manufacture)
// - constructor that initializes both fields
// - abstract method play() that returns a String
// - concrete method getInstrumentDetails() that returns information about the instrument
abstract class Instrument {
    private String name;
    protected int year;
    public Instrument(String name, int year){
        this.name=name;
        this.year=year;
    }
    abstract String play();
    public String getInstrumentDetails(){
        return "Name: "+name+", Year: "+year;
    }
}


// Step 2: Create an interface Tunable
// This should include:
// - abstract method tune() that returns a String
// - abstract method adjustPitch(boolean up) that returns a String (up means increase pitch)

interface Tunable {
    abstract String tune();
    abstract String adjustPitch(boolean up);
}

// Step 3: Create an interface Maintainable
// This should include:
// - abstract method clean() that returns a String
// - abstract method inspect() that returns a String
interface Maintainable {
    abstract String clean();
    abstract String inspect();
}

// Step 4: Create a concrete class StringedInstrument that extends Instrument
// This should include:
// - private int numberOfStrings
// - constructor that initializes name, year, and numberOfStrings
// - implementation of the abstract play() method
// - override getInstrumentDetails() to include number of strings
class StringedInstrument extends Instrument {
    private int numberOfStrings;
    public StringedInstrument(String name, int year, int numberOfStrings){
        super(name, year);
        this.numberOfStrings=numberOfStrings;
    }
    @Override
    public String play() {
        return "Playing stringed instrument";
    }

    @Override
    public String getInstrumentDetails(){
        return super.getInstrumentDetails()+", Number of Strings: "+numberOfStrings;
    }
}

// Step 5: Create a concrete class Guitar that extends StringedInstrument
// and implements Tunable and Maintainable
// This should include:
// - private String guitarType (acoustic, electric, etc.)
// - constructor that initializes all fields
// - implementation of all required interface methods
class Guitar extends StringedInstrument implements Tunable, Maintainable {
    private String guitarType;
    public Guitar(String name, int year, int numberOfStrings, String guitarType){
        super(name, year, numberOfStrings);
        this.guitarType=guitarType;
    }

    @Override
    public String tune(){
        return "tuning guitar";
    }

    @Override
    public String adjustPitch(boolean up){
        return up ? "tuning guitar up" : "tuning guitar down";
    }

    @Override
    public String clean(){
        return "cleaning guitar";
    }

    @Override
    public String inspect(){
        return "inspecting guitar";
    }
}


// Step 6: Create a concrete class Piano that extends Instrument
// and implements Tunable and Maintainable
// This should include:
// - private boolean isGrand
// - constructor that initializes all fields
// - implementation of the abstract play() method
// - implementation of all required interface methods
class Piano extends Instrument implements Tunable, Maintainable {
    private boolean isGrand;
    public Piano(String name, int year, boolean isGrand){
        super(name, year);
        this.isGrand=isGrand;
    }
    @Override
    public String play(){
        return "playing piano";
    }
    @Override
    public String tune(){
        return "tuning piano";
    }
    @Override
    public String adjustPitch(boolean up){
        return up ? "tuning piano up" : "tuning piano down";
    }
    @Override
    public String clean(){
        return "cleaning piano";
    }
    @Override
    public String inspect(){
        return "inspecting piano";
    }
}

// Step 7: Create a public class MusicShop to test the classes
// This should include:
// - main method that:
//   1. Creates an array of Instrument objects including Guitar and Piano instances
//   2. Iterates through the array calling play() for each instrument
//   3. Demonstrates polymorphism by testing if each instrument is Tunable or Maintainable
//      and if so, calls the appropriate methods

public class MusicShop {
    public static void main(String[] args){
        Instrument[] instruments = new Instrument[3];
        instruments[0] = new Guitar("Yamaha", 2026, 6, "acoustic");
        instruments[1] = new Guitar("Fire", 2025, 4, "base");
        instruments[2] = new Piano("Yamaha-700",2019, true);

        for (Instrument instrument:instruments){
            System.out.println(instrument.getInstrumentDetails());
            System.out.println(instrument.play());
            System.out.println(instrument instanceof Guitar ? ((Guitar) instrument).tune() : ((Piano) instrument).tune());
            System.out.println(instrument instanceof Guitar ? ((Guitar) instrument).inspect() : ((Piano) instrument).inspect());
        }
    }
}
