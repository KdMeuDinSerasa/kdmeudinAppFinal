package com.example.kdmeudinheiro.pieChart

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import java.lang.String

class PieChart(val parentView: View) : SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var binding: MainFragmentBinding

    fun loadChart() {
        binding = MainFragmentBinding.bind(parentView)
        //MP pie chart bind values
        binding.chartIncluded.seekBar1.setOnSeekBarChangeListener(this)
        binding.chartIncluded.seekBar2.setOnSeekBarChangeListener(this)
        binding.chartIncluded.chart1.setUsePercentValues(true);
        binding.chartIncluded.chart1.getDescription().setEnabled(false);
        binding.chartIncluded.chart1.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.chartIncluded.chart1.setDragDecelerationFrictionCoef(0.95f);

        @FontRes
        val font: Int
        font = R.font.fonts
        // binding.chartIncluded.chart1.setCenterTextTypeface(font);
        // binding.chartIncluded.chart1.setCenterText(generateCenterSpannableText());

        binding.chartIncluded.chart1.setDrawHoleEnabled(true);
        binding.chartIncluded.chart1.setHoleColor(R.color.white);

        binding.chartIncluded.chart1.setTransparentCircleColor(R.color.white);
        binding.chartIncluded.chart1.setTransparentCircleAlpha(110);

        binding.chartIncluded.chart1.setHoleRadius(58f);
        binding.chartIncluded.chart1.setTransparentCircleRadius(61f);

        binding.chartIncluded.chart1.setDrawCenterText(true);

        binding.chartIncluded.chart1.setRotationAngle(0f);
        // enable rotation of the chart by touch
        binding.chartIncluded.chart1.setRotationEnabled(true);
        binding.chartIncluded.chart1.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        binding.chartIncluded.chart1.setOnChartValueSelectedListener(this);

        binding.chartIncluded.seekBar1.setProgress(4);
        binding.chartIncluded.seekBar2.setProgress(10);

        binding.chartIncluded.chart1.animateY(1400, Easing.EaseInOutQuad);

        val l: Legend = binding.chartIncluded.chart1.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(7f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)

        // entry label styling

        // entry label styling
        binding.chartIncluded.chart1.setEntryLabelColor(R.color.white)
        // binding.chartIncluded.chart1.setEntryLabelTypeface(tfRegular)
        binding.chartIncluded.chart1.setEntryLabelTextSize(12f)

    }

    private fun setData(count: Int, range: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    //  parties.get(i % parties.length),
                    resources.getDrawable(R.drawable.ic_baseline_add_24)
                )
            )
        }
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c: Int in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(R.color.white)
        //data.setValueTypeface(tfLight)
        binding.chartIncluded.chart1.setData(data)

        // undo all highlights
        binding.chartIncluded.chart1.highlightValues(null)
        binding.chartIncluded.chart1.invalidate()
    }

    @SuppressLint("ResourceType")
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        LayoutInflater.from(requireContext()).inflate(R.menu.pie, binding.root, false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/PieChartActivity.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set: IDataSet<*> in binding.chartIncluded.chart1.getData()
                    .getDataSets()) set.setDrawValues(!set.isDrawValuesEnabled)
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.actionToggleIcons -> {
                for (set: IDataSet<*> in binding.chartIncluded.chart1.getData()
                    .getDataSets()) set.setDrawIcons(!set.isDrawIconsEnabled)
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.actionToggleHole -> {
                if (binding.chartIncluded.chart1.isDrawHoleEnabled()) binding.chartIncluded.chart1.setDrawHoleEnabled(
                    false
                ) else binding.chartIncluded.chart1.setDrawHoleEnabled(
                    true
                )
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.actionToggleMinAngles -> {
                if (binding.chartIncluded.chart1.getMinAngleForSlices() === 0f) binding.chartIncluded.chart1.setMinAngleForSlices(
                    36f
                ) else binding.chartIncluded.chart1.setMinAngleForSlices(
                    0f
                )
                binding.chartIncluded.chart1.notifyDataSetChanged()
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.actionToggleCurvedSlices -> {
                val toSet =
                    !binding.chartIncluded.chart1.isDrawRoundedSlicesEnabled() || !binding.chartIncluded.chart1.isDrawHoleEnabled()
                binding.chartIncluded.chart1.setDrawRoundedSlices(toSet)
                if (toSet && !binding.chartIncluded.chart1.isDrawHoleEnabled()) {
                    binding.chartIncluded.chart1.setDrawHoleEnabled(true)
                }
                if (toSet && binding.chartIncluded.chart1.isDrawSlicesUnderHoleEnabled()) {
                    binding.chartIncluded.chart1.setDrawSlicesUnderHole(false)
                }
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.actionDrawCenter -> {
                if (binding.chartIncluded.chart1.isDrawCenterTextEnabled()) binding.chartIncluded.chart1.setDrawCenterText(
                    false
                ) else binding.chartIncluded.chart1.setDrawCenterText(
                    true
                )
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.actionToggleXValues -> {
                binding.chartIncluded.chart1.setDrawEntryLabels(!binding.chartIncluded.chart1.isDrawEntryLabelsEnabled())
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.actionTogglePercent -> {
                binding.chartIncluded.chart1.setUsePercentValues(!binding.chartIncluded.chart1.isUsePercentValuesEnabled())
                binding.chartIncluded.chart1.invalidate()
            }
            R.id.animateX -> {
                binding.chartIncluded.chart1.animateX(1400)
            }
            R.id.animateY -> {
                binding.chartIncluded.chart1.animateY(1400)
            }
            R.id.animateXY -> {
                binding.chartIncluded.chart1.animateXY(1400, 1400)
            }
            R.id.actionToggleSpin -> {
                binding.chartIncluded.chart1.spin(
                    1000,
                    binding.chartIncluded.chart1.getRotationAngle(),
                    binding.chartIncluded.chart1.getRotationAngle() + 360,
                    Easing.EaseInOutCubic
                )
            }
            R.id.actionSave -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveToGallery()
                } else {
                    // requestStoragePermission(chart)
                }
            }
        }
        return true
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        binding.chartIncluded.tvXMax.setText(String.valueOf(binding.chartIncluded.seekBar1.getProgress()))
        binding.chartIncluded.tvYMax.setText(String.valueOf(binding.chartIncluded.seekBar2.getProgress()))
        setData(
            binding.chartIncluded.seekBar1.getProgress(),
            binding.chartIncluded.seekBar2.getProgress().toFloat()
        )
    }

    protected fun saveToGallery() {
        //  saveToGallery(binding.chartIncluded.chart1, "PieChartActivity")
    }

    @SuppressLint("ResourceAsColor")
    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(R.color.teal_700), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)
        return s
    }

    override fun onValueSelected(e: Entry?, h: Highlight) {
//        if (e == null) return
//        print().i(
//            "VAL SELECTED",
//            "Value: " + e.y + ", index: " + h.x
//                    + ", DataSet index: " + h.dataSetIndex
//        )
    }

    override fun onNothingSelected() {
//        Log.i("PieChart", "nothing selected")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

}