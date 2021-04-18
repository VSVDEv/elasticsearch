package com.vsvdev.es_link_raiting.entity;


import com.vsvdev.es_link_raiting.entiry.Link;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static com.vsvdev.es_link_raiting.entiry.Link.ago;

public class LinkTests {

    private final Link link = new Link();

    @Test
    public void setLinkDescriptionLimit() {
        assertThatIllegalArgumentException().isThrownBy(() -> link.setDescription(repeat("A", 101)));
    }

    @Test
    public void setLinkDescriptionStripsHTML() {
        link.setDescription("<b>alex</b>");
        assertThat(link.getDescription()).isEqualTo("alex");
    }

    @Test
    public void setLinkCategoryLimit() {
        assertThatIllegalArgumentException().isThrownBy(() -> link.setCategory(repeat("A", 101)));
    }

    @Test
    public void setLinkCategoryStripsHTML() {
        link.setCategory("<b>alex</b>");
        assertThat(link.getCategory()).isEqualTo("alex");
    }

    @Test
    public void setLinkTitleLimit() {
        assertThatIllegalArgumentException().isThrownBy(() -> link.setTitle(repeat("A", 101)));
    }

    @Test
    public void setLinkTitleStripsHTML() {
        link.setTitle("<b>alex</b>");
        assertThat(link.getTitle()).isEqualTo("alex");
    }

    @Test
    public void setLinkUrlLimit() {
        assertThatIllegalArgumentException().isThrownBy(() -> link.setUrl("http://example.org/" + repeat("A", 500)));
    }

    @Test
    public void setLinkUrlValid() {
        assertThatIllegalArgumentException().isThrownBy(() -> link.setUrl("not_an_url"));
    }

    @Test
    public void setLinkUrlStripsHTML() {
        link.setUrl("<b>http://example.org</b>");
        assertThat(link.getUrl()).isEqualTo("http://example.org");
    }

    @Test
    public void testValidUrls() {
        final String input = "http://example.org/2013-11-23/foo?query=parser";
        link.setUrl(input);
        assertThat(link.getUrl()).isEqualTo(input);
    }

    @Test
    public void setLinkSubmittedByLimit() {
        assertThatIllegalArgumentException().isThrownBy(() -> link.setSubmittedBy(repeat("A", 101)));
    }

    @Test
    public void setLinkSubmittedByStripsHTML() {
        link.setSubmittedBy("<b>alex</b>");
        assertThat(link.getSubmittedBy()).isEqualTo("alex");
    }

    @Test
    public void testAgo() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        assertThat(ago(now, now.minusYears(2))).isEqualTo("2 years ago");
        assertThat(ago(now, now.minusYears(1))).isEqualTo("1 year ago");
        assertThat(ago(now, now.minusMonths(2))).isEqualTo("2 months ago");
        assertThat(ago(now, now.minusMonths(1))).isEqualTo("1 month ago");
        assertThat(ago(now, now.minusDays(31))).isEqualTo("1 month ago");
        assertThat(ago(now, now.minusDays(15))).isEqualTo("15 days ago");
        assertThat(ago(now, now.minusDays(1))).isEqualTo("yesterday");
        assertThat(ago(now, now.minusDays(1).minusMinutes(10))).isEqualTo("yesterday");
        assertThat(ago(now, now.minusHours(2))).isEqualTo("2 hours ago");
        assertThat(ago(now, now.minusHours(1))).isEqualTo("1 hour ago");
        assertThat(ago(now, now.minusMinutes(10))).isEqualTo("10 minutes ago");
        assertThat(ago(now, now.minusMinutes(4))).isEqualTo("just now");
    }

    @Test
    public void testAgoFutureInput() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        assertThatIllegalArgumentException().isThrownBy(() -> ago(now, now.plusDays(1)));
    }

    public static String repeat(String s, int times) {
        StringBuilder stringBuilder = new StringBuilder(s.length() * times);
        repeats(stringBuilder, s, times);
        return stringBuilder.toString();
    }
    public static void repeats(StringBuilder stringBuilder, String s, int times) {
        if (times > 0) {
            repeats(stringBuilder.append(s), s, times - 1);
        }
    }
}
