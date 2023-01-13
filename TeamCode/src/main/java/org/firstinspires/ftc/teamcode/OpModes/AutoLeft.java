

package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Helper.Robot;

import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Left", group = "Concept")
public class AutoLeft extends LinearOpMode {

    private static final String tfodModel = "jan2023mk2";
    private static final String tfodPath = "/sdcard/FIRST/tflitemodels/" + tfodModel + ".tflite";

    private static final String[] LABELS = {
            "arrow",
            "balloon",
            "bar",
            "pole",
    };
    private static final String VUFORIA_KEY =
            "AWtcstb/////AAABmfYaB2Q4dURcmKS8qV2asrhnGIuQxM/ioq6TnYqZseP/c52ZaYTjs4/2xhW/91XEaX7c3aw74P3kGZybIaXued3nGShb7oNQyRkVePnFYbabnU/G8em37JQrH309U1zOYtM3bEhRej91Sq6cf6yLjiSXJ+DxxLtSgWvO5f+wM3Wny8MbGUpVSiogYnI7UxEz8OY88d+hgal9u3GhhISdnNucsL+fRAE8mKwT1jGDgUVE1uAJoZFvo95AJWS2Yhdq/N/HpxEH3sBXEm99ci+mdQsl0m96PMCDfV5RgWBjhLbBEIJyQ/xKAbw5Yfr/AKCeB86WDPhR3+Mr8BUvsrycZA6FDJnN5sZZwTg0ZE22+gFL";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


    private ElapsedTime runtime = new ElapsedTime();
    double timeout_ms = 0;
    public int parkingTarget = 2;
    Robot robot = new Robot();

    public enum AutoSteps {
        detectSignal, deliverPreLoad, parkFromMedium, endAuto
    }

    public AutoSteps Step = AutoSteps.detectSignal;

    @Override
    public void runOpMode() throws InterruptedException {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        robot.init(hardwareMap);
        robot.initVuforia();
        robot.initTfod();
        robot.initArmClaw();


        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (robot.tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
//                        telemetry.addData("# Objects Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display image position/size information for each one
                        // Note: "Image number" refers to the randomized image orientation/number
                        for (Recognition recognition : updatedRecognitions) {
                            String objectLabel = recognition.getLabel();
                            if (objectLabel == robot.LABELS[0]) {
                                parkingTarget = 1;
                            } else if (objectLabel == robot.LABELS[1]) {
                                parkingTarget = 2;
                            } else if (objectLabel == robot.LABELS[2]) {
                                parkingTarget = 3;
                            }
//
//                            telemetry.addData("", " ");
//                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
//                            telemetry.addData("Robot Location", robot.Location);
                            telemetry.addData("parking target", parkingTarget);
                        }
//                        telemetry.update();
                    }
                }

                switch (Step) {
                    case detectSignal:
                        telemetry.addData("Parking Target ", parkingTarget);
                        telemetry.update();
                        Step = AutoSteps.deliverPreLoad;
                        break;

                    case deliverPreLoad:
                        robot.deliverPreLoad(true);
                        Step = AutoSteps.endAuto;
                        break;

                    case parkFromMedium:
                        robot.ParkFromMedium(true, parkingTarget);
                        Step = AutoSteps.endAuto;

                    case endAuto:
                        telemetry.addData("➡️", "Auto Finished");
                        telemetry.update();
                        break;
                }
            }
        }
    }


}