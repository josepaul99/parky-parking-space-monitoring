# Parky
# _Parking Space Monitoring using Raspberry Pi_

Parky was the final year project in my Bachelor's degree which was completed in the year **2016**. It is a simple working model of parking monitoring using Raspberry Pi 2 Model B and IR sensors.  

## Screenshots
![SPLASH SCREEN](screenshot/Splash%20Screen.png "Splash Screen")
![LOGIN PAGE](screenshot/Login.png "Login Page")
![SIGNUP PAGE](screenshot/Signup%20screen.png "Signup Page")
![CHOOSE LOCATION](screenshot/Choose%20Location.png "Choose Location")
![PARKING AVAILABILITY](screenshot/Parking%20Availability.png "Parking Availability")

## Tech

###### Raspberry Pi
- The raspberry Pi 2 was installed with the raspbian OS and mysql server was setup in it.
- The database was designed to store the user details and parking slot status.
- Three IR Sensors were connected to the GPIO ports of the raspberry pi.
- A python script was written to read the IR sensor status and update the database accordingly.

###### Python
- The python script runs on the raspberry pi
- The script imports the raspberry pi GPIO library and setups the GPIO ports 17, 18 and 27
- The script also connects to the mysql server which is locally hosted.
- When a HIGH (means and object is infront of the IR sensor) is detected at the GPIO port the corresponding sensor's database value is updated to 1.
- When a LOW (no object is infront of the IR sensor) is detected at the GPIO port the corresponding sensor's database value is updated to 0.
 
###### Android
- Android app connects to the mysql database in the raspberry pi to read the sensor status.
- The total and available parking space at the chosen location is displayed to the user.
- The app has a login and signup page for authentication.

## Working Environment
As this was an academic project it was developed to work in the local home network by setting the IP of the raspberry pi to static so that the mysql server host address doesn't change on router reboot. The android phone loaded with the Parky app and raspberry pi is also connected to same wifi network so that the app can connect to mysql database. 