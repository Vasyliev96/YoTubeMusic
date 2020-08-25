package vasyliev.android.yotubemusic.db

import vasyliev.android.yotubemusic.R

data class MusicListData(
    val rawSoundId: Array<Int> = arrayOf(
        R.raw.blackpink_ddu_du_ddu_du,
        R.raw.cjipie,
        R.raw.eh,
        R.raw.houb,
        R.raw.houu,
        R.raw.hruuhb,
        R.raw.indios,
        R.raw.indios_two,
        R.raw.indios_three,
        R.raw.jah,
        R.raw.jeeh,
        R.raw.jhuee,
        R.raw.joooaah,
        R.raw.jueb,
        R.raw.juob,
        R.raw.long_scream,
        R.raw.ludovico_einaudi_bella_notte,
        R.raw.ludovico_einaudi_experience,
        R.raw.oa_h,
        R.raw.oaaaahmmm,
        R.raw.ohm_loko,
        R.raw.ramin_djavadi_point_it_black,
        R.raw.the_godfather_theme_song,
        R.raw.uehea,
        R.raw.uhraa,
        R.raw.uoh,
        R.raw.uueh
    ),
    val songName: Array<String> = arrayOf(
        "blackpink_ddu_du_ddu_du",
        "cjipie",
        "eh",
        "houb",
        "houu",
        "hruuhb",
        "indios",
        "indios_two",
        "indios_three",
        "jah",
        "jeeh",
        "jhuee",
        "joooaah",
        "jueb",
        "juob",
        "long_scream",
        "ludovico_einaudi_bella_notte",
        "ludovico_einaudi_experience",
        "oa_h",
        "oaaaahmmm",
        "ohm_loko",
        "ramin_djavadi_point_it_black",
        "the_godfather_theme_song",
        "uehea",
        "uhraa",
        "uoh",
        "uueh"
    )
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MusicListData

        if (!rawSoundId.contentEquals(other.rawSoundId)) return false
        if (!songName.contentEquals(other.songName)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rawSoundId.contentHashCode()
        result = 31 * result + songName.contentHashCode()
        return result
    }
}