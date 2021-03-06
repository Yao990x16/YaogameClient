package pres.yao.yaogame.client.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pres.yao.client.R
import pres.yao.client.databinding.MineFragmentBinding

class MineFragment : Fragment() {

    companion object {
        fun newInstance() = MineFragment()
    }

    private var viewBinding: MineFragmentBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = MineFragmentBinding.inflate(layoutInflater, container, false)
        Log.e("fragOncreview",arguments.toString())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*val bundle = arguments
        var email = bundle?.getParcelable<User>("User")?.email
        var username = bundle?.getParcelable<User>("User")?.username*/
        binding.mineEmail.setData(LoginActivity.userEmail)
        binding.mineUsername.setData(LoginActivity.userName)
        if (LoginActivity.userName!=null){
            binding.mineLoginOrOut.text = "注销"
            binding.mineSubscription.setOnClickListener {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        binding.mineLoginOrOut.setOnClickListener {
            if (binding.mineLoginOrOut.text=="登录"){
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }else{
                /*username=null
                email=null*/
                LoginActivity.userName=null
                LoginActivity.userEmail=null
                binding.mineEmail.setData(LoginActivity.userName)
                binding.mineUsername.setData(LoginActivity.userEmail)
                binding.mineLoginOrOut.text="登录"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**
         * Fragment的存在时间比其视图长,所以要在onDestroyView方法中消除对绑定类实例的所有引用
         */
        viewBinding = null
    }
}