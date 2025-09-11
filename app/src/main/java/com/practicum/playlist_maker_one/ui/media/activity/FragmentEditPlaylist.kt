package com.practicum.playlist_maker_one.ui.media.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentEditPlaylistBinding
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.viewModel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class FragmentEditPlaylist : Fragment(){

    private lateinit var binding: FragmentEditPlaylistBinding
    private var playlist: PlayListData? = null
    private var imageUri : Uri? = null
    private val viewModel : EditPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(EXTRA_EDIT_PLAYLIST, PlayListData::class.java)
        } else {
            @Suppress("DEPRECATION")
            (arguments?.getParcelable(EXTRA_EDIT_PLAYLIST))
        }
        setView()

        val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.photo.setPadding(0,0,0,0)
                binding.photo.visibility = View.VISIBLE
                imageUri = uri
                Glide.with(view)
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
                binding.saveButton.isEnabled = binding.nameInput.text.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.saveButton.setOnClickListener {
            val newImageUri = if(imageUri != null){
                saveImageToStorage(imageUri!!, binding.nameInput.text.toString())
            }else{
                playlist!!.imageUrl
            }

            val newPlayList = PlayListData(
                id = playlist!!.id,
                name = binding.nameInput.text.toString(),
                description = binding.descriptionInput.text.toString(),
                imageUrl = newImageUri,
                tracks = playlist!!.tracks,
                tracksCount = playlist!!.tracksCount
            )

            viewModel.updatePlaylist(newPlayList)

            //!!новая фишка - используем result API для передачи обновления
            val result = bundleOf("updated_playlist" to newPlayList)
            parentFragmentManager.setFragmentResult("edit_playlist_request", result)

            Toast.makeText(requireContext(), getString(R.string.playList) + playlist!!.name + getString(R.string.edited), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.leaveButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun  setView(){
        val paddingInPx = 24.dpToPx(requireContext())
        binding.nameInput.setText(playlist?.name)

        if(playlist?.imageUrl != ""){
            Glide.with(requireContext())
                .load(playlist?.imageUrl)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(resources.getDimensionPixelSize(R.dimen.posterCornerRadius))
                    )
                )
                .into(binding.photo)
        }
        else{
            binding.photo.setPadding(paddingInPx,paddingInPx,paddingInPx,paddingInPx)
            binding.photo.setImageResource(R.drawable.ic_placeholder_45)
        }

        if(playlist?.description != ""){
            binding.descriptionInput.setText(playlist?.description)
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


    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    companion object {
        private const val EXTRA_EDIT_PLAYLIST = "PLAYLIST_EDIT_EXTRA"

        fun createPlaylist(playlist: PlayListData): Bundle = bundleOf(EXTRA_EDIT_PLAYLIST to playlist)
    }
}