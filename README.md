  This project was created in order to implement skills of creating and retrieving data from 
sensors, creating and updating widgets, get location of device and interactions between
fragments and activitiy

  Contains 3 fragments, widget and MainActivity.
  Main activity implements logic for caliing different fragments. Asks
for permission of location.
  Coordinates fragment finds azimuth, pitch and poll by magnetometer and accelerometer sensors.
Sets the data in fragment, updates data
  Location fragment implements play services for getting location. Updates widget 
with data of location.
  Text fragment creates a small fragment on the top of the screen. Data from textView 
from activity is passed and can be changed. After clicking the button "close text fragment"
text from fragment is passed back to activity
