package hu.bets.apigateway.service;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ClubBadgeResolverServiceTest {

    public static final String REAL_MADRID = "Real Madrid";
    public static final String FC_BASEL = "FC Basel";
    public static final String GALATASARAY = "Galatasaray";
    public static final String PARMA_FC = "Parma FC";
    public static final String FERENCVAROSI_TC = "Ferencvarosi TC";
    public static final String MACCABI_HAIFA = "Maccabi Haifa";
    public static final String LA_PAZ_FC = "La Paz FC";
    public static final String ALIANZA_FC = "Alianza FC";
    public static final String TAURO_FC = "Tauro FC";
    public static final String AL_JAZIRA_CLUB = "Al Jazira Club";
    public static final String STANDARD_FK = "Standard FK";
    public static final String ATLANTA = "Atlanta";
    public static final String SINGAPORE_ARMED_FORCES_FC = "Singapore Armed Forces FC";
    public static final String TEMA_YOUTH = "Tema Youth";
    public static final String DOXA_DAPAMAS = "Doxa Dapamas";
    public static final String PUSKAS_AKADEMIA = "Puskas Akademia";
    private ClubBadgeResolverService sut = new ClubBadgeResolverService();

    @Before
    public void before() {
        sut.init();
    }

    @Test
    public void shouldReturnAMapOfResolvedClubs() {
        Map<String, String> badges = sut.resolveBadges(Arrays.asList(REAL_MADRID, FC_BASEL, GALATASARAY, PARMA_FC,
                FERENCVAROSI_TC, MACCABI_HAIFA, LA_PAZ_FC,
                ALIANZA_FC, TAURO_FC, AL_JAZIRA_CLUB, STANDARD_FK, ATLANTA, SINGAPORE_ARMED_FORCES_FC,
                TEMA_YOUTH, DOXA_DAPAMAS, PUSKAS_AKADEMIA));

        assertEquals("2", badges.get(REAL_MADRID));
        assertEquals("195", badges.get(FC_BASEL));
        assertEquals("249", badges.get(GALATASARAY));
        assertEquals("346", badges.get(PARMA_FC));
        assertEquals("474", badges.get(FERENCVAROSI_TC));
        assertEquals("545", badges.get(MACCABI_HAIFA));
        assertEquals("627", badges.get(LA_PAZ_FC));
        assertEquals("736", badges.get(ALIANZA_FC));
        assertEquals("816", badges.get(TAURO_FC));
        assertEquals("971", badges.get(AL_JAZIRA_CLUB));
        assertEquals("1275", badges.get(STANDARD_FK));
        assertEquals("1322", badges.get(ATLANTA));
        assertEquals("1461", badges.get(SINGAPORE_ARMED_FORCES_FC));
        assertEquals("1525", badges.get(TEMA_YOUTH));
        assertEquals("1605", badges.get(DOXA_DAPAMAS));
        assertEquals("99999", badges.get(PUSKAS_AKADEMIA));

    }

    @Test
    public void shouldCorrectlyResolveNamesToCrests() {
        assertEquals("2", sut.resolveBadge(REAL_MADRID));
        assertEquals("195", sut.resolveBadge(FC_BASEL));
        assertEquals("249", sut.resolveBadge(GALATASARAY));
        assertEquals("346", sut.resolveBadge(PARMA_FC));
        assertEquals("474", sut.resolveBadge(FERENCVAROSI_TC));
        assertEquals("545", sut.resolveBadge(MACCABI_HAIFA));
        assertEquals("627", sut.resolveBadge(LA_PAZ_FC));
        assertEquals("736", sut.resolveBadge(ALIANZA_FC));
        assertEquals("816", sut.resolveBadge(TAURO_FC));
        assertEquals("971", sut.resolveBadge(AL_JAZIRA_CLUB));
        assertEquals("1275", sut.resolveBadge(STANDARD_FK));
        assertEquals("1322", sut.resolveBadge(ATLANTA));
        assertEquals("1461", sut.resolveBadge(SINGAPORE_ARMED_FORCES_FC));
        assertEquals("1525", sut.resolveBadge(TEMA_YOUTH));
        assertEquals("1605", sut.resolveBadge(DOXA_DAPAMAS));
        assertEquals("99999", sut.resolveBadge(PUSKAS_AKADEMIA));
    }
}
