package com.rizieq.whatsapp.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rizieq.whatsapp.R
import com.rizieq.whatsapp.utils.Constants.DATA_IMAGES
import com.rizieq.whatsapp.utils.Constants.DATA_USERS
import com.rizieq.whatsapp.utils.Constants.DATA_USER_EMAIL
import com.rizieq.whatsapp.utils.Constants.DATA_USER_IMAGE_URL
import com.rizieq.whatsapp.utils.Constants.DATA_USER_NAME
import com.rizieq.whatsapp.utils.Constants.DATA_USER_PHONE
import com.rizieq.whatsapp.utils.Constants.REQUEST_CODE_PHOTO
import com.rizieq.whatsapp.utils.User
import com.rizieq.whatsapp.utils.populateImage
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_profile.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class ProfileActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDb = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private var imageUrl: String? = null
    private var nama : String? = null
    private var imail : String? = null
    private var pone : String? = null
    private var pass : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if (userId.isNullOrEmpty()) {
            finish()
        }
        progress_layout.setOnTouchListener { v, event -> true }
        btn_apply.setOnClickListener {
            onApply()
        }
        btn_delete_account.setOnClickListener {
            onDelete()
        }
        imbtn_profile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
        }

        populateInfo()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            storeImage(data?.data) // method storeImage dijalankan setelah pengguna memilih gambar
        }
    }


    private fun storeImage(uri: Uri?) {
        if (uri != null) {
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
            progress_layout.visibility = View.VISIBLE
            val filePath = firebaseStorage.child(DATA_IMAGES).child(userId!!)

            filePath.putFile(uri)
                .addOnSuccessListener {
                    filePath.downloadUrl
                        .addOnSuccessListener {
                            val url = it.toString()
                            firebaseDb.collection(DATA_USERS)
                                .document(userId)
                                .update(DATA_USER_IMAGE_URL, url)
                                .addOnSuccessListener {
                                    imageUrl = url
                                    populateImage(
                                        this, imageUrl, img_profile,
                                        R.drawable.ic_user
                                    )
                                }
                            progress_layout.visibility = View.GONE
                        }
                        .addOnFailureListener {
                            onUploadFailured()
                        }
                }
                .addOnFailureListener {
                    onUploadFailured()
                }
        }    }

    private fun onUploadFailured() {
        Toast.makeText(this, "Image upload failed. Please try again later.", Toast.LENGTH_SHORT).show()
        progress_layout.visibility = View.GONE
    }


    private fun populateInfo() {
        progress_layout.visibility = View.VISIBLE
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {
                val user = it?.toObject(User::class.java)
                imageUrl = user?.imageUrl
                nama = user?.name
                imail = user?.email
                pone = user?.phone
                pass = user?.pass

                edt_name_profile.setText(nama, TextView.BufferType.EDITABLE)
                edt_email_profile.setText(imail, TextView.BufferType.EDITABLE)
                edt_phone_profile.setText(pone, TextView.BufferType.EDITABLE)
                if (imageUrl != null) {
                    populateImage(
                        this, user?.imageUrl, img_profile, R.drawable.ic_user)
                }
                progress_layout.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                finish()
            }

    }

    private fun onDelete() {
        progress_layout.visibility = View.VISIBLE

        val confirm = layoutInflater.inflate(R.layout.confirm_layout,null, false)

        AlertDialog.Builder(this)
            .setView(confirm)
            .setTitle("Delete Account")
            .setMessage("This will delete your Profile Information. Are you sure?")
            .setPositiveButton("Yes") { dialog, which ->
                firebaseDb.collection(DATA_USERS).document(userId!!).delete()
                firebaseStorage.child(DATA_IMAGES).child(userId).delete()
                firebaseAuth.currentUser?.delete()
                    ?.addOnSuccessListener {
                        finish()
                        movetologin()
                    }
                    ?.addOnFailureListener {
                        finish()
                    }
                progress_layout.visibility = View.GONE
                Toast.makeText(this, "Profile deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, which ->
                progress_layout.visibility = View.GONE
            }
            .setCancelable(false)
            .show()

    }

    private fun movetologin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun onApply() {

        progress_layout.visibility = View.VISIBLE
        val name = edt_name_profile.text.toString()
        val email = edt_email_profile.text.toString()
        val phone = edt_phone_profile.text.toString()
        val map = HashMap<String, Any>()

        map[DATA_USER_NAME] = name
        map[DATA_USER_EMAIL] = email
        map[DATA_USER_PHONE] = phone

        if (nama == name && imail == email && pone == phone) {
            Toast.makeText(this, "Belum ada data yang diubah", Toast.LENGTH_SHORT).show()
            progress_layout.visibility = View.GONE
        } else {
            firebaseDb.collection(DATA_USERS).document(userId!!).update(map) // perintah update
                .addOnSuccessListener {
                    Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                    progress_layout.visibility = View.GONE
                }
        }
    }
}