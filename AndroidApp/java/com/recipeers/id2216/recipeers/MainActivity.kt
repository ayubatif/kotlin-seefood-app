package com.recipeers.id2216.recipeers
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.recipeers.id2216.recipeers.Friends.ActionFeed
import com.recipeers.id2216.recipeers.Friends.FriendsList
import com.recipeers.id2216.recipeers.SearchRecipes.SearchRecipesActivity
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.fragment_food_card.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener,NavigationView.OnNavigationItemSelectedListener,
    RecentTab.OnFragmentInteractionListener, ActionFeed.OnFragmentInteractionListener, CreateRecipe.OnFragmentInteractionListener, ItemGrid.OnFragmentInteractionListener,
    FoodCard.OnFragmentInteractionListener, Messages.OnFragmentInteractionListener, MessageBox.OnFragmentInteractionListener, FriendsList.OnFragmentInteractionListener,
    FavoritesGrid.OnFragmentInteractionListener, FavoritesViewer.OnFragmentInteractionListener, CommentPage.OnFragmentInteractionListener, 
    OtherUserProfile.OnFragmentInteractionListener{
      
    companion object {
        fun logOutIntent(context: Context): Intent {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, Authentication::class.java)
            return intent
        }

        var rand: Random = Random()
    }
    private lateinit var auth: FirebaseAuth

    private val CAMERA_REQUEST_CODE = 12345
    private val REQUEST_GALLERY_CAMERA = 54654
    private var mSavedBitmap: Bitmap? = null
    private lateinit var bitmapML: Bitmap
    private lateinit var LabelList: List<FirebaseVisionCloudLabel>

    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private var ingredients: ArrayList<String> = ArrayList()
    private var db = FirebaseFirestore.getInstance()
    private var areIngredientsLoaded: Boolean = false
    private var userID: String = ""
    private var checkIsOn = false
    private var messageTime: Date? = null
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        mUser =FirebaseAuth.getInstance().currentUser
        if(mUser == null){
            val intent = Intent(this, Authentication::class.java)
            startActivity(intent)
        } else{
            setSupportActionBar(bar)
            fab.setOnClickListener(this)
            userID = auth.currentUser!!.uid
            setupHeader()

            db.collection("Ingredients/")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            ingredients.add(document.id)
                        }
                        areIngredientsLoaded = true
                    } else {
                        Log.e("lool", "error")
                    }
                }


            if (savedInstanceState == null) {
                var transaction = supportFragmentManager.beginTransaction()
                mSavedBitmap = savedInstanceState?.getParcelable("savedBitmap")
                //image.setImageBitmap(mSavedBitmap)
                transaction.add(R.id.fragment_view_main,   ItemGrid.newInstance(), "ItemGrid")
                    .commit()
            }
        }
        createNotificationChannel()
        timeCheck()
        //notificationMessageOpen()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "test name"
            val descriptionText = "test description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelID = R.string.default_notification_channel_id.toString()
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun timeCheck() {
        val messageInbox = db.collection("Users").document(auth.currentUser!!.uid).collection("Notification").document("Message")
        messageInbox.get().addOnSuccessListener { documentSnapshot ->
            val time = documentSnapshot.get("Time")
            if (time != null) {
                messageTime = time as Date
            }
        }
    }

    fun notificationCheck() {
        val messageInbox = db.collection("Users").document(auth.currentUser!!.uid).collection("Notification").document("Message")
        messageInbox.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            Log.e("test", "listener running")
            if (e != null) {
                Log.e("test", "Listen failed.", e)
                return@EventListener
            }
            else {
                if (messageTime != snapshot?.get("Time")) {
                    Log.e("test", "listen success")
                    Log.e("test", snapshot.toString())
                    var sender = snapshot?.get("Sender")
                    var message = snapshot?.get("Message")
                    Log.e("test", sender.toString())
                    Log.e("test", message as String)
                    Log.e("test", messageTime.toString())
                    Log.e("test", snapshot?.get("Time").toString())
                    MyFirebaseMessagingService.displayNotification(this, sender as String, message as String)
                }
            }
            /*
            taken from:
            https://stackoverflow.com/questions/49837447/kotlin-how-to-pass-a-data-between-two-fragments
             */
        })
    }

    override fun onPause() {
        super.onPause()
        if (!checkIsOn) {
            Log.e("test", "notification is on")
            notificationCheck()
        }
    }

    fun tokenViewer() {
        var tokenMaybe: String
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            tokenMaybe = task.result!!.token
            Log.e("test", tokenMaybe)
            var data: MutableMap<String, Any> = HashMap()
            data.put("TokenFCM", tokenMaybe)
            db.collection("Users").document(auth.uid.toString()).set(data, SetOptions.merge())
        }
    }

    fun setupHeader(){
        navigation.setNavigationItemSelectedListener(this)
        var view = navigation.getHeaderView(0)
        view.findViewById<TextView>(R.id.sideBarUsername).text = auth.currentUser!!.displayName
        db.collection("Users").document(userID).addSnapshotListener({ snapshot, e ->
            GlideApp.with(this)
                .load(Uri.parse(snapshot!!.get("Image").toString()))
                .placeholder(R.color.GreyTransparent)
                .into(view.findViewById<ImageView>(R.id.sideBarProfilePicture))
        })



    }
    override fun changeToGrid() {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
        transaction.replace(R.id.fragment_view_main, ItemGrid.newInstance())
        transaction.addToBackStack(null);
        transaction.commit()
    }


    override fun goToMessageBox(Box: DocumentReference) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
        transaction.replace(R.id.fragment_view_main, MessageBox.newInstance(Box))
        transaction.addToBackStack(null);
        transaction.commit()
    }

    override fun changeToProfile(profile: MutableMap<String, Any>?, authorID: String) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
        transaction.replace(R.id.fragment_view_main, OtherUserProfile.newInstance(profile, authorID))
        transaction.addToBackStack(null);
        transaction.commit()
    }


    fun changeToCreate(){
        if(areIngredientsLoaded){
            var transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
            transaction.replace(R.id.fragment_view_main, CreateRecipe.newInstance(ingredients))
            transaction.addToBackStack(null);
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        changeToGrid()
    }

    fun changeToFavorite() {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_right)
        transaction.replace(R.id.fragment_view_main, FavoritesGrid.newInstance())
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onFoodItemClicked(food: MutableMap<String, Any>?, foodID: String) {
        if(areIngredientsLoaded){
            var transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
            transaction.replace(R.id.fragment_view_main, FoodCard.newInstance(food, foodID))
            transaction.addToBackStack(null);
            transaction.commit()
            //backButton.visibility = View.VISIBLE
        }
    }
    override fun onClick(v: View?) {
        when (v) {

            fab -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA), REQUEST_GALLERY_CAMERA
                    )
                } else {
                    openCamera()
                }
            }
            backButton -> {
                if(backButton.visibility == View.VISIBLE){
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
                    transaction.replace(R.id.fragment_view_main, ItemGrid.newInstance())
                    transaction.addToBackStack(null);
                    transaction.commit()
                    backButton.visibility = View.GONE
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.bottom_bar_favorite -> {
                changeToFavorite()
            }
            R.id.bottom_bar_search -> {
                Toast.makeText(
                    baseContext, "search Clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.bottom_bar_add ->{
                changeToCreate()
                }
            android.R.id.home -> drawer.openDrawer(GravityCompat.START)

        }

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sidebarHome -> {
                drawer.closeDrawer(GravityCompat.START)
                var transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
                transaction.replace(R.id.fragment_view_main, ItemGrid.newInstance())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.sidebarMessages -> {
                drawer.closeDrawer(GravityCompat.START)
                var transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
                transaction.replace(R.id.fragment_view_main, Messages.newInstance())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.sidebarFriendsList -> {
                drawer.closeDrawer(GravityCompat.START)
                var transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
                transaction.replace(R.id.fragment_view_main, FriendsList.newInstance())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.sidebarLogout -> startActivity(logOutIntent(this))
        }

        return true
    }

    fun goToAccount(v: View?) {
        val intent = Intent(this, Account::class.java)
        startActivity(intent)
    }



    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_GALLERY_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this@MainActivity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val extras = data?.extras
                    mSavedBitmap = extras?.get("data") as Bitmap
                    if (mSavedBitmap != null) {
                        Log.e("savedBitmap", "lol")
                        val intent = Intent(this, SearchRecipesActivity::class.java)
                        intent.putExtra("CAPTURED_BITMAP", mSavedBitmap)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, getString(R.string.null_bitmap), Toast.LENGTH_SHORT).show()
                    }
                    // can run new activity here immediatly after capturing an image
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("savedBitmap", mSavedBitmap)
    }
}
