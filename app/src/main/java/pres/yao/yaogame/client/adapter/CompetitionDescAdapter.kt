package pres.yao.yaogame.client.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pres.yao.client.R
import pres.yao.yaogame.client.model.data.Competition
import pres.yao.yaogame.client.view.LoginActivity
import pres.yao.yaogame.client.viewmodel.SubscribeViewModel

class CompetitionDescAdapter(private val competitionList: List<Competition>):
    RecyclerView.Adapter<CompetitionDescAdapter.ViewHolder>(),View.OnClickListener {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val competitionId: TextView = view.findViewById(R.id.competition_id)
        val competitionDate: TextView = view.findViewById(R.id.competition_date)
        val matchDescTV: TextView = view.findViewById(R.id.match_desc)
        val competitionDateTimeTV: TextView = view.findViewById(R.id.competition_date_time)
        val leftTeamNameTV: TextView = view.findViewById(R.id.left_team_name)
        val leftTeamIconIV: ImageView = view.findViewById(R.id.left_team_icon)
        val scoreTV: TextView = view.findViewById(R.id.score)
        val rightTeamIconIV: ImageView = view.findViewById(R.id.right_team_icon)
        val rightTeamNameTV: TextView = view.findViewById(R.id.right_team_name)
        //订阅按钮
        val buttonSubscribeBT: Button = view.findViewById(R.id.button_subscribe)
        //查看预测等信息
        val buttonShowPredictBT: Button = view.findViewById(R.id.button_show_predict)
        //分割线
        val descZhankaiIB: ImageButton = view.findViewById(R.id.desc_zhankai)
        init{
            buttonSubscribeBT.setOnClickListener(this@CompetitionDescAdapter)
            buttonShowPredictBT.setOnClickListener(this@CompetitionDescAdapter)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.competition_rv_row_desc_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val competition = competitionList[position]
        //比赛id
        holder.competitionId.text = competition.competition_id
        //2021-04-30 20:00:00
        //比赛日期
        if(position==0){
            holder.competitionDate.visibility=View.VISIBLE
            holder.competitionDate.text = competition.start_time?.substring(0,10)
        }else{
            holder.competitionDate.visibility = View.GONE
        }
        holder.competitionDate.tag = position
        //比赛时间
        holder.competitionDateTimeTV.text = competition.start_time?.substring(11,16)
        holder.competitionDateTimeTV.tag = position
        //比赛轮次
        holder.matchDescTV.text = competition.game_stage
        holder.matchDescTV.tag = position
        //左边队伍名称/图标
        holder.leftTeamNameTV.text = competition.left_name
        holder.leftTeamNameTV.tag = position
        Glide.with(holder.itemView)
            .load(competition.left_icon)
            .error(R.drawable.ic_vector_boy)
            .into(holder.leftTeamIconIV)
        holder.leftTeamIconIV.tag = position
        holder.scoreTV.text = competition.left_score.toString()+":"+competition.right_score.toString()
        holder.scoreTV.tag = position
        //右边队伍图标/名称
        Glide.with(holder.itemView)
            .load(competition.right_icon)
            .error(R.drawable.ic_vector_gril)
            .into(holder.rightTeamIconIV)
        holder.rightTeamIconIV.tag = position
        holder.rightTeamNameTV.text = competition.right_name
        holder.rightTeamNameTV.tag = position
        //分割线
       holder.descZhankaiIB.tag = position
        //订阅和预测按钮
        if(holder.matchDescTV.text.contains("NBA")){
            holder.buttonShowPredictBT.visibility = View.VISIBLE
        }else{
            holder.buttonShowPredictBT.visibility = View.GONE
        }
        competition.competition_id?.let { setSubsText(holder, it) }
        holder.buttonSubscribeBT.tag = position
        holder.buttonShowPredictBT.tag = position
    }

    override fun getItemCount(): Int {
        return competitionList.size
    }

    private  val subviewModel =  SubscribeViewModel()
    private fun setSubsText(holder: ViewHolder, compId: String){
        LoginActivity.userName?.let { it ->
            subviewModel.getByUserNameAndCompId(it,compId)
                .subscribe {
                    if (it.msg=="没有订阅"){
                        holder.buttonSubscribeBT.text="订阅"
                    }else{
                        holder.buttonSubscribeBT.text="取消订阅"
                    }
                }
        }
    }
    //=======================以下为item中的button控件点击事件处理===================================
    //item里面有多个控件可以点击（item+item内部控件）
    enum class ViewName {
        ITEM, PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    interface OnItemClickListener {
        fun onItemClick(v: View?, viewName: ViewName?, position: Int)
        fun onItemLongClick(v: View?)
    }
    //声明自定义的接口
    private var mOnItemClickListener: OnItemClickListener? = null

    //定义方法并传给外面的使用者
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    override fun onClick(v: View) {
        //getTag()获取数据
        val position = v.tag as Int
        if (mOnItemClickListener != null) {
            if (v.id==R.id.rcv_home_frg && v.id == R.id.rcv_esports_frg && v.id==R.id.rcv_sports_frg) {
                mOnItemClickListener!!.onItemClick(v, ViewName.PRACTISE, position)
            } else {
                mOnItemClickListener!!.onItemClick(v, ViewName.ITEM, position)
            }
        }
    }
}