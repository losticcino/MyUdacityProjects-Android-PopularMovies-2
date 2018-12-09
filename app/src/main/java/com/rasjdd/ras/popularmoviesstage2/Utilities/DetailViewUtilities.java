package com.rasjdd.ras.popularmoviesstage2.Utilities;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.rasjdd.ras.popularmoviesstage2.R;

public class DetailViewUtilities {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void SetSynopsisText(TextView textView, String s) {
        StyleSpan boldTypeface = new StyleSpan(Typeface.BOLD);

        SpannableStringBuilder sbSynopsis = new SpannableStringBuilder(
                context.getString(R.string.details_heading_synopsis) + ": ");
        sbSynopsis.setSpan(boldTypeface, 0,
                context.getString(R.string.details_heading_synopsis).length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sbSynopsis.append("\n\t\t").append(s);
        textView.setText(sbSynopsis);
    }

    public void SetRating(TextView textView, float f) {
        int ratingColor = (Integer) new ArgbEvaluator().evaluate((f / 10 + 0.1f),
                context.getColor(R.color.colorBad), context.getColor(R.color.colorGood));
        textView.setTextColor(ratingColor);
        textView.setText(String.valueOf(f));
    }

    public void SetMoreInfo(TextView textView, String releaseDate, String originalLanguage, Boolean isAdult){
        String s = context.getString(R.string.details_heading_release) + ": ";
        s += releaseDate + Constants.textJoint;
        s += originalLanguage;
        if (isAdult) s += Constants.textJoint + context.getString(R.string.details_movie_is_adult);
        textView.setText(s);
    }
}
