package com.b11a.android.autosaver.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.b11a.android.autosaver.MainActivity
import com.b11a.android.autosaver.databinding.ActivityJoinContactBinding
import com.b11a.android.autosaver.kPrefs
import com.b11a.android.autosaver.login.models.UserDetail

class JoinContactActivity : AppCompatActivity() {
    val userToken by lazy { kPrefs(this).getString("userToken", "")!! }

    private lateinit var binding: ActivityJoinContactBinding
    private lateinit var viewModel: JoinContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinContactBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(JoinContactViewModel::class.java)
        setContentView(binding.root)

        viewModel.initializeDetail(intent.getSerializableExtra("detail") as UserDetail)

        viewModel.joining.observe(this) {
            if (it) {
                binding.loadingLayout.root.visibility = View.VISIBLE
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.loadingLayout.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }

        viewModel.success.observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewModel.error.observe(this) {
            binding.textErrorContact.text = it
        }

        binding.editTextNameContact.addTextChangedListener {
            viewModel.setEmergencyName(it.toString())
        }

        binding.editTextNumberContact.addTextChangedListener {
            viewModel.setEmergencyNumber(it.toString())
        }

        binding.buttonSkipContact.setOnClickListener {
            viewModel.setEmergencyName("")
            viewModel.setEmergencyNumber("")
            viewModel.submit(userToken)
        }

        binding.buttonCompletionContact.setOnClickListener {
            viewModel.submit(userToken)
        }
    }
}