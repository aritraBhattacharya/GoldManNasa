package com.aritra.goldmannasa.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.aritra.goldmannasa.R
import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.network.utils.Status
import com.aritra.goldmannasa.databinding.FragmentApodBinding
import com.aritra.goldmannasa.presentation.utils.DUMMY_TEXT_FOR_ERROR
import com.aritra.goldmannasa.presentation.utils.MEDIA_TYPE_IMAGE
import com.aritra.goldmannasa.presentation.utils.MEDIA_TYPE_VIDEO
import com.aritra.goldmannasa.presentation.utils.RETRY_TEXT
import com.aritra.goldmannasa.presentation.viewmodel.vm.ApodViewModel
import com.aritra.goldmannasa.utils.ENTER_DATE_TO_SEARCH
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ApodFragment : Fragment() {


    private var _binding: FragmentApodBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchEditText: TextView
    private val viewModel: ApodViewModel by activityViewModels()
    private var currentAPOD: APODEntity?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentApodBinding.inflate(inflater, container, false)
        val root: View = binding.root
        searchEditText = binding.apodEt
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchEditText.setOnClickListener {
            showDatePicker()
        }
        binding.apodSearchBtn.setOnClickListener {
            if (binding.apodEt.text.isNullOrEmpty()) {
                Toast.makeText(context, ENTER_DATE_TO_SEARCH, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getDatedAPOD(binding.apodEt.text.toString())
            }
        }
        binding.favBtn.setOnClickListener {
            currentAPOD?.let {
                viewModel.saveAPODToFavorites(it)
            }
        }
    }


    private fun showDatePicker() {
        val datePickerBuilder = MaterialDatePicker
            .Builder
            .datePicker()
            .setTitleText(R.string.date_picker_title)
            .setCalendarConstraints(CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now()).build())
            .build()

        datePickerBuilder.show(requireActivity().supportFragmentManager, "tag")
        datePickerBuilder.addOnPositiveButtonClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = Date(it)
            val selectedDate = dateFormat.format(date)
            searchEditText.text = selectedDate
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.apod.observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        with(binding) {
                            it.data?.let { aopdEntity ->
                                currentAPOD = aopdEntity
                                binding.apodEt.text = aopdEntity.date
                                apodDate.text = aopdEntity.date
                                apodExplanation.text = aopdEntity.explanation
                                apodTitle.text = aopdEntity.title
                                showMedia(aopdEntity.media_type, aopdEntity.url)

                                if(aopdEntity.isFavourite){
                                    favBtn.setImageResource(R.drawable.ic_fav)
                                }
                                else{
                                    favBtn.setImageResource(R.drawable.ic_not_fav)
                                }

                            }
                            gifSearchProgress.visibility = View.GONE
                        }

                    }
                    Status.ERROR -> {
                        it.message?.let {msg-> showSnackBarWithRetryOption(msg)}
                        showErrorView()
                        binding.gifSearchProgress.visibility = View.GONE

                    }
                    Status.LOADING -> {
                        binding.gifSearchProgress.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showErrorView() {
        binding.apply {
            playerView.visibility = View.GONE
            apodImg.visibility = View.VISIBLE
            apodImg.setImageResource(R.drawable.ic_image_place_holder)
            apodImg.scaleType= ImageView.ScaleType.CENTER_INSIDE
            apodDate.text = DUMMY_TEXT_FOR_ERROR
            apodTitle.text = DUMMY_TEXT_FOR_ERROR
            apodExplanation.text = DUMMY_TEXT_FOR_ERROR
        }
    }

    private fun showSnackBarWithRetryOption(message: String) {
        val snackBar =
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
                setAction(RETRY_TEXT) {
                    binding.apodEt.text.toString().let { etText->
                        if(etText.isNullOrEmpty()) viewModel.getLatestAPOD()
                        else viewModel.getDatedAPOD(etText)
                    }
                }
            }.also { it.show() }
    }

    private fun showMedia(mediaType: String, url: String) {

        when (mediaType) {
            MEDIA_TYPE_IMAGE -> {
                binding.apply {
                    playerView.visibility = View.GONE
                    apodImg.visibility = View.VISIBLE
                    apodImg.scaleType = ImageView.ScaleType.FIT_XY
                    Glide.with(requireContext())
                        .load(url)
                        .centerInside()
                        .transition(withCrossFade(500))
                        .error(R.drawable.ic_search)
                        .into(apodImg)
                }

            }
            MEDIA_TYPE_VIDEO -> {
                binding.apply {
                    playerView.visibility = View.VISIBLE
                    apodImg.visibility = View.GONE
                    playerView.settings.javaScriptEnabled = true
                    playerView.loadUrl(url)
                }
            }
            else -> {// set error view
            }
        }

    }
}


