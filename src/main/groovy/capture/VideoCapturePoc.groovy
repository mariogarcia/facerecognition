package capture

import org.openimaj.io.*
import org.openimaj.math.geometry.shape.*

import org.openimaj.image.*
import org.openimaj.image.colour.*
import org.openimaj.image.model.*
import org.openimaj.feature.*
import org.openimaj.image.feature.local.engine.*
import org.openimaj.image.processing.alignment.*
import org.openimaj.image.processing.edges.*
import org.openimaj.image.processing.face.alignment.RotateScaleAligner
import org.openimaj.image.processing.face.recognition.*
import org.openimaj.image.processing.face.detection.*
import org.openimaj.image.processing.face.detection.keypoints.*

import org.openimaj.ml.annotation.*
import org.openimaj.util.pair.*
import org.openimaj.tools.localfeature.*

import org.openimaj.video.*
import org.openimaj.video.capture.*


/*
video.each { MBFImage v ->
    DisplayUtilities.displayName(v.process(new HaarCascadeDetector(80)), "videoFrames")
    def faces = fd. detectFaces (Transforms.calculateIntensity(v));
    println faces
}
*/

class VideoCapturePoc {

void doImageProcessing() {

    // Create face stuff
    FKEFaceDetector faceDetector = new FKEFaceDetector(new HaarCascadeDetector());
    EigenFaceRecogniser<KEDetectedFace, Person> faceRecognizer =
        EigenFaceRecogniser.
            create(
                20,
                new RotateScaleAligner(),
                1,
                DoubleFVComparison.CORRELATION,
                0.9f);

    FaceRecognitionEngine<KEDetectedFace, Extractor<KEDetectedFace>, Person> faceEngine =
        FaceRecognitionEngine.create(
            faceDetector,
            faceRecognizer);

    // Start loop
    def video = new VideoCapture(320,240)
    def display = VideoDisplay.createVideoDisplay(video)

    video.each { MBFImage fimg ->

        List<KEDetectedFace> faces = faceEngine.getDetector().detectFaces(fimg.flattenMax());

        // Go through detected faces
        for (KEDetectedFace face : faces) {

            println faces.size()

            // Find existing person for this face
            Person person = null;
            try {

                List<IndependentPair<KEDetectedFace, ScoredAnnotation<Person>>> rfaces =
                    faceEngine.recogniseBest(face.getFacePatch());

                ScoredAnnotation<Person> score = rfaces.get(0).getSecondObject();

                if (score != null)
                    person = score.annotation;

            } catch (Exception e) {
            }

            // If not found, create
            if (person == null) {

                // Create person
                person = new Person();
                println("Identified new person: " + person.getIdentifier());

                // Train engine to recognize this new person
                faceEngine.train(person, face.getFacePatch());

            } else {

                // This person has been detected before
                println("Identified existing person: " + person.getIdentifier());

            }

        }

    }

}

    static void main(String[] args) {
        new VideoCapturePoc().doImageProcessing()
    }

}

