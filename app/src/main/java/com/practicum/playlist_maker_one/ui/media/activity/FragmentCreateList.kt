package com.practicum.playlist_maker_one.ui.media.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlist_maker_one.databinding.FragmentCreatePlayListBinding
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.ui.media.viewModel.CreateListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class FragmentCreateList : Fragment() {

    private lateinit var binding: FragmentCreatePlayListBinding
    private var imageUri : Uri? = null
    private val viewModel: CreateListViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createButton.isEnabled = false
        binding.photo.visibility = View.GONE
        binding.photoPlaceholder.visibility = View.VISIBLE

        val alert = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.are_you_sure_pay_list))
            .setMessage(getString(R.string.exit_warning_play_list))
            .setNeutralButton(getString(R.string.cansel)){ dialog, which ->

            }
            .setPositiveButton(getString(R.string.end)){dialog, which ->
                findNavController().navigateUp()
            }

        val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
            if(uri != null ){
                binding.photo.visibility = View.VISIBLE
                binding.photoPlaceholder.visibility = View.GONE
                imageUri = uri
                Glide.with(this)
                    .load(uri)
                    .transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(resources.getDimensionPixelSize(R.dimen.posterCornerRadius))
                        )
                    )
                    .into(binding.photo)
            }
        }

        binding.photoBorders.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.nameInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                binding.createButton.isEnabled = binding.nameInput.text.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.createButton.setOnClickListener {
            val newImageUri = if(imageUri != null){
                saveImageToStorage(imageUri!!, binding.nameInput.text.toString())
            }else{
                ""
            }

            val playList = PlayListData(
                id = 0,
                name = binding.nameInput.text.toString(),
                description = binding.descriptionInput.text.toString(),
                imageUrl = newImageUri,
                tracksIds = emptyList(),
                tracksCount = 0
            )

            viewModel.savePlayList(playList)

            Toast.makeText(requireContext(), getString(R.string.playList) + playList.name + getString(R.string.done), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.leaveButton.setOnClickListener {
            alert.show()
        }
    }

    private fun saveImageToStorage(uri : Uri, name : String) : String{
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_covers")

        if(!filePath.exists()){
            filePath.mkdirs()
        }

        val timeStamp = System.currentTimeMillis()
        val file = File(filePath, "cover_${name}_${timeStamp}.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

        return  file.absolutePath
    }
}