package pt.ulusofona.lp2.deisichess

class StatisticsKt() {


    fun getStatsCalculator(statType: StatType) : (GameManager)->ArrayList<String>{
         when(statType){
            StatType.TOP_5_CAPTURAS -> return ::getTop5Capturas
            StatType.TOP_5_PONTOS -> return ::getTop5Pontos
            StatType.PECAS_MAIS_5_CAPTURAS -> return ::getPeca5Capturas
            StatType.PECAS_MAIS_BARALHADAS -> return ::getPecaMaisBaralhada
            StatType.TIPOS_CAPTURADOS -> return ::getTiposCapturados
        }

    }

    fun getTop5Capturas(gameManager: GameManager) : ArrayList<String> {
        val myList  = ArrayList<String>()
        myList.add("val")
        myList.add("norma")
        myList.add("dd")
        return myList
    }

    fun getTop5Pontos(gameManager: GameManager) : ArrayList<String>{

        val myList  = ArrayList<String>()
        myList.add("val");
        myList.add("norma")
        myList.add("dd")
        return myList
    }

    fun getPeca5Capturas(gameManager: GameManager) : ArrayList<String>{
        val myList  = ArrayList<String>()
        myList.add("val");
        myList.add("norma")
        myList.add("dd")
        return myList
    }

    fun getPecaMaisBaralhada(gameManager: GameManager) : ArrayList<String>{
        val myList  = ArrayList<String>()
        myList.add("val");
        myList.add("norma")
        myList.add("dd")
        return myList
    }

    fun getTiposCapturados(gameManager: GameManager) : ArrayList<String>{

        val myList  = ArrayList<String>()
        myList.add("val");
        myList.add("norma")
        myList.add("dd")
        return myList
    }





}