package com.it.openmechanic.ui.publish

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.it.openmechanic.data.model.Advertisement
import com.it.openmechanic.data.model.Response.*
import com.it.openmechanic.databinding.FragmentPublishBinding
import com.it.openmechanic.ui.base.BaseMVVMFragment
import com.it.openmechanic.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class PublishFragment : BaseMVVMFragment<FragmentPublishBinding, PublishViewModel>() {

    override val viewModel: PublishViewModel by sharedViewModel()

    private val advertisement by lazy {
        Advertisement()
    }

    override fun setupView(): Unit = with(binding) {
        addPhotoBtn.setOnClickListener {
            if (isFreeImageHolder()) selectImage()
        }

        publishBtn.setOnClickListener {
            storeImages()
        }
    }

    override fun observe() {
        viewModel.storeImageResponse.observe(this) uriObserver@{
            it?.let {
                advertisement.images.add(it.toString())
                return@uriObserver
            }
            addAdvertisement()
        }

        viewModel.addAdvertisementResponse.observe(this) {
            when (it) {
                is Success -> {
                    viewModel.hideProgress()
                    toast("You successfully published advertisement")
                }
                is Loading -> viewModel.showProgress()
                is Error -> {
                    viewModel.hideProgress()
                    toast(it.message)
                }
            }
        }
    }

    private fun addAdvertisement() = with(binding) {
        with(advertisement) {
            address = addressEtx.text.toString()
            description = desc.text.toString()
            timeStamp = Date().toString()
        }
        viewModel.addAdvertisement(advertisement)
    }

    private fun storeImages() {
        val uris = getImageUrls()
        viewModel.storeImages(uris)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    data.data?.let { showImage(it) }
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    data.data?.let { showImage(it) }
                }
            }
        }
    }

    private fun showImage(uri: Uri) = with(binding) {
        when {
            photoOne.drawable != null -> {
                photoOne.setImageURI(uri)
                photoOne.tag = uri
            }
            photoTwo.drawable != null -> {
                photoTwo.tag = uri
                photoTwo.setImageURI(uri)
            }
            photoThree.drawable != null -> {
                photoThree.tag = uri
                photoThree.setImageURI(uri)
            }
            else -> toast("Only three of them permitted")
        }
    }


    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->

            if (options[item] == "Take Photo") {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)

            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)

            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun isFreeImageHolder(): Boolean = with(binding) {
        return photoOne.drawable != null
                || photoTwo.drawable != null
                || photoThree.drawable != null
    }

    private fun getImageUrls(): ArrayList<Uri> = with(binding) {
        val uris = arrayListOf<Uri>()
        if (photoOne.drawable != null) uris.add(Uri.parse(photoOne.tag.toString()))
        if (photoTwo.drawable != null) uris.add(Uri.parse(photoTwo.tag.toString()))
        if (photoThree.drawable != null) uris.add(Uri.parse(photoThree.tag.toString()))
        return uris
    }
}