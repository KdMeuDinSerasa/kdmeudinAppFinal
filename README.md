
<h1 align="center">WhereIsMyMoney</h1>

## Languages
- [English](https://github.com/KdMeuDinSerasa/kdmeudinAppFinal/blob/main/README.md)
- [Português](https://github.com/KdMeuDinSerasa/kdmeudinAppFinal/blob/main/Readme_PT-BR.md)

## APK Beta
[Link](https://drive.google.com/file/d/15w7MBMkJKgamNX24or0cSU86bQ8D2abK/view?usp=sharing)


## Project Description
<p align="center">An application to show how the user spends his incomes and with hints of how to improve his
financial life</p>


<h4 align="center"> 
	🚧   work in progress - Check develop Branch for the latest updates  🚧
</h4>


### Functionalities

- [x] User Registration/Auth in firebase
- [x] Chart showing a quick demonstration of expends
- [x] CRUD in Bills
- [x] API for news about economy and business.
- [x] User profile and preferences.
- [x] Push Notifications.


### Requirements 

- Android Studio
- Java JDK
- AVD or Android SmartPhone later than API level 23/ Android 6.0 Marshmallow

## How to use the APP
<h4 align="center"> 
	🚧 🚨  Atention - the application uses internet 🚨 🚧 
</h4>

<div>
<p> 1º Dowload the app beta apk at, drive. (Link on ##APK beta) </p>
 <p>2º run the downloaded apk to install </p>
 <p>3º at first, you have two options, Sign-in or Sign-up, you may create an account for that.  </p>
	<img width="225" alt="Screen Shot 2021-09-20 at 11 39 03" src="https://user-images.githubusercontent.com/80124800/134022391-4df4bd77-7b50-4135-9225-e96de09c76af.png">
<img width="229" alt="Screen Shot 2021-09-20 at 11 42 16" src="https://user-images.githubusercontent.com/80124800/134022406-bbe33111-3052-4baa-b4da-430220a6a570.png">


 <p>just for test finality we have an fake user that you guys can use.  </p>
 <p>email: janeDoe@gmail.com </p>
 <p>password: DoeJane21 </p>
 <p>3.1º on Sign-up screen you have an checkbox to save your user and stay logged </p>
 <p>4º so now you are at the app, at right side upwords you will find a plus sign, click to add you Monthly Income.  </p>
 <p>5º at the bottom menu bar, you will find tow buttons, one leads you to the news letter and the other to bills crud. </p>
 <p>5.1º clicking on the left one, will lead you to a news letter with the latest business and finacial articles. </p>
 <p>5.2º clicking on the right one, will lead you to bills crud. </p>
 <p>6º so, at the crud bills, you may notice two buttons, a plus where you can add your bills, and an "?" where is a about, to gide you at your journey here. </p>
			<img width="355" alt="Screen Shot 2021-09-20 at 10 26 12" src="https://user-images.githubusercontent.com/80124800/134010628-d6475ca4-cc63-49f1-a7a7-2b911c8f0200.png">
<p>7º if you have bills listed, you can click on it and will apper a details where you can edit or delete, and change a status thats tags the bill to payed.  </p>
	<img width="328" alt="Screen Shot 2021-09-20 at 10 27 38" src="https://user-images.githubusercontent.com/80124800/134010437-5d4e3f89-1724-4d9b-b3b2-a578f67e8bb2.png">

<p>8º now back to dashboard you will find an menu icon up left, on click will open an drawer menu, listing some info abaout yor user and contains a logout button and a user preferences button.</p>
	<img width="328" alt="Screen Shot 2021-09-20 at 11 06 02" src="https://user-images.githubusercontent.com/80124800/134016448-d20f2490-c794-44aa-87e9-76e18807e5ee.png"><img width="322" alt="Screen Shot 2021-09-20 at 11 07 34" src="https://user-images.githubusercontent.com/80124800/134016725-cf94ec45-c44b-40a0-9b5f-46fb0f936ad1.png">


<p>9º clicking at user prefs you will be redirected to user preferences page where you can choose some prefres like recieve push notification, you can change you name as well and click at user avatar its possible to upload a photo to from you galery. </p>
</div>



### Dependencies 
    
    //LottieFiles
    implementation "com.airbnb.android:lottie:3.4.0"

    //hilt
    implementation "com.google.dagger:hilt-android:2.37"
    implementation 'com.google.firebase:firebase-storage-ktx:20.0.0'
    implementation 'com.google.firebase:firebase-database-ktx:20.0.2'
    implementation 'com.google.firebase:firebase-messaging-ktx:22.0.0'
    kapt "com.google.dagger:hilt-compiler:2.37"

    //Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'

    //Workmanager
    implementation("androidx.work:work-runtime-ktx:2.5.0")

    //Glide
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    
    //Firebase
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'com.google.firebase:firebase-firestore:23.0.3'
    implementation 'com.google.firebase:firebase-firestore-ktx:23.0.3'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
    //Navigation
    implementation 'android.arch.navigation:navigation-fragment-ktx:1.0.0'
    implementation 'android.arch.navigation:navigation-ui-ktx:1.0.0'
    implementation "com.google.android.material:material:1.1.0"
    //Mp Android Charts
    implementation 'com.github.PhilJay:MPAndroidChart:v2.2.4'

    // Circle Images view.
    implementation 'de.hdodenhof:circleimageview:3.1.0'

    //unit test
    testImplementation "com.google.truth:truth:1.0.1"
    androidTestImplementation "com.google.truth:truth:1.0.1"

    //retrofit
    implementation 'com.google.code.gson:gson:2.8.7'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'


### 🛠 Technologies 

- [Kotlin](https://kotlinlang.org)
- [Firebase](https://firebase.google.com/)
- [Retrofit](https://square.github.io/retrofit/) 
- [LottieFiles](https://lottiefiles.com)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Glide](https://github.com/bumptech/glide)
- [Circle Image View](https://github.com/hdodenhof/CircleImageView)

### Authors
- Name: Edson Neto
- email: edsonneto2533@gmail.com
- [Linkedin](https://www.linkedin.com/in/edson-neto-55779b167/)

----------------------------
- Name: Giovanni Raposo
- email: g.raposo@outlook.com.br
- [Linkedin](https://www.linkedin.com/in/giovanni-raposo-pinheiro/)

----------------------------

- Name: Andrei Campigotto
- email: andreicampigotto@icloud.com
- [Linkedin](https://www.linkedin.com/in/andrei-campigotto-45168255/)


### License 
- [MIT](https://github.com/KdMeuDinSerasa/kdmeudinAppFinal/blob/main/License)


