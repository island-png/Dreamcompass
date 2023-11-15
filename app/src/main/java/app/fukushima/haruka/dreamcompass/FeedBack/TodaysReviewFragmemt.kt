package app.fukushima.haruka.dreamcompass.FeedBack

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.databinding.FragmentTodaysReviewBinding

class TodaysReviewFragmemt : Fragment(R.layout.fragment_todays_review) {
    private var _binding: FragmentTodaysReviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentTodaysReviewBinding.bind(view)

//        binding.buttonNext.setOnClickListener {
//            findNavController().navigate(R.id.action_todaysReviewFragmemt_to_tomorrowTodoFragment)
//        }
    }

}

