package com.example.kdmeudinheiro.bottomSheet

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.adapter.AdapterChartTips
import com.example.kdmeudinheiro.databinding.TipChartBinding
import com.example.kdmeudinheiro.interfaces.ChartClickInterceptor
import com.example.kdmeudinheiro.model.Articles
import com.google.android.material.bottomsheet.BottomSheetDialog


class BottomSheetChart(
    val parentView: View,
    val typeClicked: Int,
    val clickInterceptor: ChartClickInterceptor,
    val articleList: List<Articles>
) {
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: TipChartBinding

    private lateinit var recyclerView: RecyclerView
    private var adapter = AdapterChartTips() {
        clickInterceptor.interceptSelectedArticle(it)
    }

    fun loadBottomSheet() {
        /* setup bottom sheet */
        bottomSheetView = View.inflate(parentView.context, R.layout.tip_chart, null)
        bottomSheetDialog = BottomSheetDialog(parentView.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetBinding = TipChartBinding.bind(bottomSheetView)
        /* setup recyclerview */

        recyclerView = bottomSheetBinding.recyclerViewIdTipChart
        recyclerView.layoutManager = LinearLayoutManager(bottomSheetView.context)
        recyclerView.adapter = adapter

        /* filter to show based at parameter */
        if (typeClicked == 4 /* fix */) {
            bottomSheetBinding.textViewTipChart.text =
                bottomSheetView.context.getString(R.string.text_tip_chart_fix)
            bottomSheetBinding.recyclerViewIdTipChart.visibility = View.GONE
            bottomSheetBinding.materialCardForChartTips.visibility = View.VISIBLE
            bottomSheetBinding.webViewList.loadUrl("https://www.serasa.com.br/ensina/como-ganhar-dinheiro/")
        } else if (typeClicked == 5 /* leisure */) {
            bottomSheetBinding.textViewTipChart.text = "Lazer" //TODO string for this
            bottomSheetBinding.materialCardForChartTips.visibility = View.GONE
            var list = articleList.filter { it.typeArticle.toInt() == 1 }
            adapter.update(list)
        } else if (typeClicked == 6 /* emergency */) {
            bottomSheetBinding.textViewTipChart.text = "Emergenciais" //TODO string for this
            bottomSheetBinding.materialCardForChartTips.visibility = View.GONE
            var list = articleList.filter { it.typeArticle.toInt() == 2 }
            adapter.update(list)
        } else if (typeClicked == 7 /* monthly */) {
            bottomSheetBinding.textViewTipChart.text =
                bottomSheetView.context.getString(R.string.text_tip_chart_monthly)
            bottomSheetBinding.recyclerViewIdTipChart.visibility = View.GONE
            bottomSheetBinding.materialCardForChartTips.visibility = View.VISIBLE
            bottomSheetBinding.webViewList.loadUrl("https://www.serasa.com.br/ensina/suas-economias/")
        }
    }
}
