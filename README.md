# Android Course Rating App With Firebase Integration
This app allows users to rate courses available at KEA.


## Firebase integration
This app uses Google Firebase/Firestore for files and users.
Currently it holds and structured like

	* courses
		* Fields
			* courseName
			* courseDesc
		* courseReview
			* UserID
				* rating 1
				* rating 2
				* rating 3
				* rating n
				* course grade
				* final note
	* Course Questions
		*Fields
			* question 1
			* question 2
			* question 3
			* question N

## Recycler View
In order to display the data from firestore, the app uses the android recycler view


