package org.firstinspires.ftc.teamcode.OpModes;
//imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Helper.new_robot;

@TeleOp(name = "22-23 TeleOp", group = "LinearOpMode")

public class new_teleop extends LinearOpMode {

    //tells you how long the robot has run for
    private ElapsedTime runtime = new ElapsedTime();


    new_robot robot = new new_robot();

    //How fast your robot will accelerate.
    public double acceleration = 0.3;

    //Motor powers
    public double fl_power = 0;
    public double bl_power = 0;
    public double fr_power = 0;
    public double br_power = 0;

    public double DRIVETRAIN_SPEED = 0.6;



    @Override
    public void runOpMode() throws InterruptedException {
        /**
         * Instance of Robot class is initalized
         */
        robot.init(hardwareMap);

        /**
         * This code is run during the init phase, and when opMode is not active
         * i.e. When "INIT" Button is pressed on the Driver Station App
         */
        waitForStart();



        while (opModeIsActive()) {

            double move_y_axis = gamepad1.left_stick_y;
            double move_x_axis = -gamepad1.left_stick_x;
            double pivot_turn = -gamepad1.right_stick_x;

            //Sets the target power
            double target_fl_power = move_y_axis + move_x_axis + pivot_turn;
            double target_bl_power = move_y_axis - move_x_axis + pivot_turn;
            double target_fr_power = move_y_axis - move_x_axis - pivot_turn;
            double target_br_power = move_y_axis + move_x_axis - pivot_turn;

            //Adds how far you are from target power, times acceleration to the current power.
            fl_power += acceleration * (target_fl_power - fl_power);
            bl_power += acceleration * (target_bl_power - bl_power);
            fr_power += acceleration * (target_fr_power - fr_power);
            br_power += acceleration * (target_br_power - br_power);

            /**
             Drivetrain
             */

            robot.FLMotor.setPower(DRIVETRAIN_SPEED * fl_power);
            robot.BLMotor.setPower(DRIVETRAIN_SPEED * bl_power);
            robot.FRMotor.setPower(DRIVETRAIN_SPEED * fr_power);
            robot.BRMotor.setPower(DRIVETRAIN_SPEED * br_power);

            /** Claw **/
            if(gamepad1.x) {
                robot.claw.setPosition(90);
            }
            if(gamepad1.y) {
                robot.claw.setPosition(0);
            }

            /** Slider **/
            double vSliderPower =  -gamepad1.left_stick_y;
            double hSliderPower = gamepad1.right_stick_x;

            robot.vSlider.setPower(vSliderPower);
            robot.hSlider.setPower(hSliderPower);

        }

    }
}
