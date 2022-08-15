package com.example.raise_developer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TutorialRecyclerAdapter(private val bgColors: ArrayList<Int>) : RecyclerView.Adapter<TutorialRecyclerAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val pageName: TextView = itemView.findViewById(R.id.pageName)

        fun bind(position: Int) {
            if (position == 0){
                pageName.text = "- 게임 설명\n\n" +
                        "1. “개발자 키우기”는 여러 시스템을 통해 개인 자산을 모아 레벨을 올리고 물품을 사는 것을 목표로 하는 게임입니다. \n\n" +
                        "2. 연봉은 일정 시간마다 모래 시계 버튼에 쌓입니다. \n\n" +
                        "3. 집 안을 클릭하면 터치수당을 얻을 수 있으며, 가운데 모래 시계 버튼을 클릭하면 쌓인 연봉을 얻을 수 있습니다. \n\n" +
                        "4. 일정 시간마다 IT 관련 4문항의 객관식 퀴즈가 도착하고, 정답 여부에 따라 일정 효과를 받습니다. \n\n" +
                        "5. 상점의 직원은 고용 시 집 안과 인벤토리에 추가 되며 레벨을 올릴 수 있습니다. \n\n" +
                        "6. 상점의 차와 시계는 구매 시 인벤토리에 추가 됩니다. \n\n" +
                        "7. 레벨은 상승 시 명시된 효과를 받으며 일정 레벨은 조건 만족 시 도달할 수 있습니다. \n\n" +
                        "8. 잔디 페이지는 GITHUB와 연결되어 자신의 잔디를 확인할 수 있고 수확할 수 있습니다. \n\n" +
                        "9. 본 게임은 인터넷 이용을 필수로 하고 있음에 주의 부탁드립니다."
            }
            else{
                pageName.text = "- 제작자의 글\n\n" +
                        "\n\n" +
                        "안녕하세요. 스테이지어스 팀원 김재걸, 조성민, 한만욱입니다. \n\n" +
                        "\n\n" +
                        "개발 초기 깃허브 잔디 시각화에 대한 생각이 저희를 게임 개발까지 끌고 왔네요. 코틀린으로 게임을 만들 무모한 사람들은 아마 전세계를 뒤져봐도 많지 않을 것 같습니다. 그만큼 탈도 많고 정말 희노애락을 모두 겪었던 개발 과정이었습니다. \n\n" +
                        "\n\n" +
                        "아마 게임을 플레이 하시면 많은 재미를 얻기는 힘들 겁니다. 처음에는 레벨이나 각종 물품을 구매하는 것에 무리가 없지만 일정 수준에 도달하면 난이도가 번지 점프를 할 예정이니까요. \n\n" +
                        "\n\n" +
                        "애시당초 저희의 목적은 이 게임의 만렙 도달은 곧 취업이라는 목표 의식과 함께 여러 요소들로 개발에 관한 동기부여를 주는 것이었답니다.(물론, 지금 이 글을 보기 전까지는 알지 못하셨겠지만…) \n\n" +
                        "\n\n" +
                        "게임의 끝을 보는 길은 너무나도 험난한 여정이겠지만, 가장 확실한 건 지금 이 글을 보고 계시는 사용자 분들은 끝내 게임을 클리어하고 그동안의 노력이 담긴 열매를 맺으실 예정이라는 것입니다."
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view,
            parent,
            false
        )
        return PagerViewHolder(view)
    }
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = bgColors.size
}
