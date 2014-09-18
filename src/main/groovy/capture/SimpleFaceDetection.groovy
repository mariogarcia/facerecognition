package capture

import org.openimaj.image.*
import org.openimaj.image.colour.*
import org.openimaj.image.processing.face.recognition.*
import org.openimaj.image.processing.face.detection.*

import org.openimaj.video.*
import org.openimaj.video.capture.*

class SimpleFaceDetection {
    static void main(args) {
        // Start loop
        VideoCapture video = new VideoCapture(320,240)
        VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video)

        display.addVideoListener(new VideoDisplayListener<MBFImage>() {
            public void beforeUpdate(MBFImage frame) {
                FaceDetector<DetectedFace,FImage> faceDetector =
                    new HaarCascadeDetector(40)

                List<DetectedFace> faces =
                    faceDetector.detectFaces(Transforms.calculateIntensity(frame))

                faces.each { DetectedFace face ->
                    frame.drawShape(face.bounds, RGBColour.RED)
                }

            }
            public void afterUpdate(VideoDisplay<MBFImage> dsi) {

            }
        })
    }
}

