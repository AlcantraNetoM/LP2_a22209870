package pt.ulusofona.lp2.deisichess

class StatisticsKt() {




    fun getTop5Capturas(gameManager: GameManager) : ArrayList<String> {
        return ArrayList()
    }

    fun getTop5Pontos(gameManager: GameManager) : ArrayList<String>{

        return ArrayList()
    }

    fun getPeca5Capturas(gameManager: GameManager) : ArrayList<String>{
        return ArrayList()
    }

    fun getPecaMaisBaralhada(gameManager: GameManager) : ArrayList<String>{
        return ArrayList()
    }

    fun getTiposCapturados(gameManager: GameManager) : ArrayList<String>{

        return ArrayList()
    }



    fun getStatsCalculator(statType: StatType) : Function1<GameManager,ArrayList<String>>{
        return when(statType){
            StatType.TOP_5_CAPTURAS -> ::getTop5Capturas
            StatType.TOP_5_PONTOS -> ::getTop5Pontos
            StatType.PECAS_MAIS_5_CAPTURAS -> ::getPeca5Capturas
            StatType.PECAS_MAIS_BARALHADAS -> ::getPecaMaisBaralhada
            StatType.TIPOS_CAPTURADOS -> ::getTiposCapturados
        }

    }

}