package capture

import org.openimaj.ml.annotation.*

class Person extends ScoredAnnotation<Person> implements Serializable{

    Long id

    Person() {
        super(null, 0.0f)
    }

    Person(Person person, float coincidence) {
        super(person, coincidence)
    }

}
