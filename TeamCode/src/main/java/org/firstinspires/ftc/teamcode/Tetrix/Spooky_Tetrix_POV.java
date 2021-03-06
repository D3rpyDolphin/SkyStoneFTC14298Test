package org.firstinspires.ftc.teamcode.Tetrix;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.ftccommon.SoundPlayer;
import android.media.MediaPlayer;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.R;

@TeleOp(name = "Spooky_Tetrix_POV", group="Tetrix")
@Disabled
public class Spooky_Tetrix_POV extends OpMode {

    private DcMotor right_drive;
    private DcMotor left_drive;
    private DcMotor lift;

    private Servo left_hand;
    private Servo right_hand;

    private double ServoPosition = 0;
    private double ServoPosition2 = 1;
    private double ServoSpeed = 0.1;
    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    

    //The player handling the audio
    private static MediaPlayer player = null;

    public void init() {
        player = MediaPlayer.create(hardwareMap.appContext, R.raw.spooky);
    }

    public void stop() {
        player.stop();
    }

    public void loop() {
        // Motors
        right_drive = hardwareMap.dcMotor.get("right_drive");
        left_drive = hardwareMap.dcMotor.get("left_drive");
        lift = hardwareMap.dcMotor.get("lift");

        // Servos
        left_hand = hardwareMap.servo.get("left_hand");
        right_hand = hardwareMap.servo.get("right_hand");

        // Reverse one of the drive motors.
        // You will have to determine which motor to reverse for your robot.
        // In this example, the right motor was reversed so that positive
        // applied power makes it move the robot in the forward direction.
        right_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        // Put loop blocks here.
        // Left Bumper opens
        if (gamepad1.left_bumper) {
            ServoPosition += ServoSpeed;
            ServoPosition2 += -ServoSpeed;
        }
        // Right Bumper closes
        if (gamepad1.right_bumper) {
            ServoPosition += -ServoSpeed;
            ServoPosition2 += ServoSpeed;
        }

        if (gamepad1.b) {
            player.start();
        }

        if (player.isPlaying() && gamepad1.x) {
            player.pause();
            player.seekTo(0);
        }

        if (gamepad1.dpad_up) {
            lift.setPower(1);
        } else {
            lift.setPower(0);
        }

        if (gamepad1.dpad_down) {
            lift.setPower(-0.3);
        } else {
            lift.setPower(0);
        }

        ServoPosition = Math.min(Math.max(ServoPosition, 0), 1);
        ServoPosition2 = Math.min(Math.max(ServoPosition2, 0), 1);

        left_hand.setPosition(ServoPosition);
        right_hand.setPosition(ServoPosition2);

        double control_left = gamepad1.left_stick_y - gamepad1.right_stick_x;
        double control_right = gamepad1.left_stick_y + gamepad1.right_stick_x;

        if (control_left < 0) {
            control_left = -1 * Math.pow(gamepad1.left_stick_y - gamepad1.right_stick_x, 2);
        } else {
            control_left = Math.pow(gamepad1.left_stick_y - gamepad1.right_stick_x, 2);
        }

        if (control_right < 0) {
            control_right = -1 * Math.pow(gamepad1.left_stick_y + gamepad1.right_stick_x, 2);
        } else {
            control_right = Math.pow(gamepad1.left_stick_y + gamepad1.right_stick_x, 2);
        }

        left_drive.setPower(control_left);
        right_drive.setPower(control_right);

        telemetry.addData("Left Pow", left_drive.getPower());
        telemetry.addData("Right Pow", right_drive.getPower());
        telemetry.addData("Lift Pow", lift.getPower());
        telemetry.addData("Servo", ServoPosition);
        telemetry.addData("Servo2", ServoPosition2);
        telemetry.update();
    }
}
