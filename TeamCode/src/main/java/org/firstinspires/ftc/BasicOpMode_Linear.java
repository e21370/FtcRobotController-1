/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")
//@Disabled
public class BasicOpMode_Linear extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftTopDrive = null;
    private DcMotor leftBottomDrive = null;
    private DcMotor rightBottomDrive = null;
    private DcMotor rightTopDrive = null;
    private DcMotor armDrive = null;
    private Servo handClampServo = null;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftTopDrive  = hardwareMap.get(DcMotor.class, "left_top_drive");
        rightTopDrive = hardwareMap.get(DcMotor.class, "right_top_drive");
        leftBottomDrive = hardwareMap.get(DcMotor.class, "left_bottom_drive");
        rightTopDrive = hardwareMap.get(DcMotor.class, "right_bottom_drive");
        armDrive = hardwareMap.get(DcMotor.class, "arm_drive");
        handClampServo = hardwareMap.get(Servo.class, "hand_clamp");
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftTopDrive.setDirection(DcMotor.Direction.REVERSE);
        rightTopDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBottomDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBottomDrive.setDirection(DcMotor.Direction.FORWARD);
        armDrive.setDirection(DcMotor.Direction.FORWARD);
        handClampServo.setDirection(Servo.Direction.FORWARD);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        //1. define variables speed_ratio and turn_ratio
        double speed_ratio = 1;
        double turn_ratio = 1;

        //double handTurnPosition = handTurnServo.getPosition();

        double handClampPosition = handClampServo.getPosition();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.dpad_up == true){
                speed_ratio = 1;
            }
            if (gamepad1.dpad_down == true){
                speed_ratio = 0.25;
            }
            if (gamepad1.dpad_left == true){
                turn_ratio = 1;
            }
            if (gamepad1.dpad_right == true){
                turn_ratio = 0.25;
            };
            if (gamepad1.x == true){
                speed_ratio = 0.5;
                turn_ratio = 0.5;
            }
            //2. read gamepad x,y,a,b
            //assign values for speed_ratio and turn_ratio

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;
            double armPower;
            double handClampPower;
            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.

            //step 3: calculate power using speed_ratio and turn_ratio
            double drive = (-gamepad1.left_stick_y) * speed_ratio;
            double turn  =  (gamepad1.right_stick_x) * turn_ratio;
            armPower = (gamepad2.left_stick_y) * 0.5;
            handClampPower = (gamepad2.right_stick_y) * 0.01;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            // armPower = Range.clip(arm, -1.0, 1.0) ;
            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            leftTopDrive.setPower(leftPower);
            rightTopDrive.setPower(rightPower);
            armDrive.setPower(armPower);

            handClampPosition += handClampPower;
            handClampServo.setPosition(handClampPosition);
            sleep(50);
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Servos", "turn (%.2f), clamp (%.2f)", handClampPosition);
            telemetry.update();
            
            //since im not completely sure how to merge everything together im just gonna write some stuff here
            
        }
    }
}
