package pt.ulusofona.lp2.deisichess

class Statistics {

    fun getTop5Capturas() : ArrayList<String> {
        return ArrayList()
    }

    fun getTop5Pontos() : ArrayList<String>{
        return ArrayList()
    }

    fun getPeca5Capturas() : ArrayList<String>{
        return ArrayList()
    }

    fun getPecaMaisBaralhada() : ArrayList<String>{
        return ArrayList()
    }

    fun getTiposCapturados() : ArrayList<String>{
        return ArrayList()
    }



    fun getStatsCalculator(statType: StatType) : () -> ArrayList<String>{
        return when(statType){
            StatType.TOP_5_CAPTURAS -> ::getTop5Capturas
            StatType.TOP_5_PONTOS -> ::getTop5Pontos
            StatType.PECAS_MAIS_5_CAPTURAS -> ::getPeca5Capturas
            StatType.PECAS_MAIS_BARALHADAS -> ::getPecaMaisBaralhada
            StatType.TIPOS_CAPTURADOS -> ::getTiposCapturados
        }

    }

}