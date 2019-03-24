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

## Sign in
The app uses a user sign in system with email. Right now there is a user hardcoded into the fields, but other do exsist, and its only there for a fast sign in. Android also keeps the user sign in, even after you close the app.

## Recycler View
In order to display the data from firestore, the app uses the android recycler view.
To use the recycler view you have to use models, adapters, holders and inflaters.
 - this sort of reminds of the way spring does it.

## Rating a course
This is the biggest part of the app, with alot of functionality.

## Mail a rating
As part of the requirement the app had to send a mail to the school about the rating. If fields are valid the user has to have a Gmail app or another app able to send a mail. When the user click on the "Mail" button a app choser pops up, and a user can pick the app they want to use. The mail is automaticly filled out with sender,reciver,subject and the rating for the course.
### If fields are empty a toast pop up asking the user for input.



