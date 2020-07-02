package kr.hs.emirim.sagittta.historendar.decorators;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DayOfWeek;

public class SundayDecorator implements DayViewDecorator {

    @Override public boolean shouldDecorate(final CalendarDay day) {
        final DayOfWeek weekDay = day.getDate().getDayOfWeek();
        return weekDay == DayOfWeek.SUNDAY;
    }

    @Override public void decorate(final DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.rgb(255, 92, 92)));
    }
}
