package com.trip.tripsync.ui.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentCoinBinding
import com.trip.tripsync.databinding.FragmentLoginBinding
import com.trip.tripsync.databinding.FragmentSignupBinding
import java.util.Timer

class CoinFragment : Fragment() {

    private var _binding: FragmentCoinBinding? = null
    private val binding: FragmentCoinBinding
        get() = _binding!!
    private var state: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoinBinding.inflate(inflater, container, false)

        val coinBtn = binding.coinBtn
        val coinImage = binding.coinImage
        binding.coinText.setImageResource(R.drawable.coin_text_start)

        coinBtn.setOnClickListener {
            val anim: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.coin_effect)
            coinImage.startAnimation(anim)
            Handler(Looper.getMainLooper()).postDelayed( {
                val media: MediaPlayer? = MediaPlayer.create(requireContext(), R.raw.coin_effect)
                state = (0..1).random()
                if (state == 0) {
                    coinImage.setImageResource(R.drawable.coin_back)
                    binding.coinText.setImageResource(R.drawable.coin_text_no)
                    media?.start()
                } else {
                    coinImage.setImageResource(R.drawable.coin_front)
                    binding.coinText.setImageResource(R.drawable.coin_text_yes)
                    media?.start()
                }
            }, 2000)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.coinCharacter.setImageResource(R.drawable.coin_img02)
            }, 300)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.coinCharacter.setImageResource(R.drawable.coin_img03)
            }, 2000)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.coinCharacter.setImageResource(R.drawable.coin_img01)
                binding.coinText.setImageResource(R.drawable.coin_text_start)
            }, 3500)
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //doSomething
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CoinFragment {
            return CoinFragment()
        }
    }
}