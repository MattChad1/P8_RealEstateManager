package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.datas.database.PrepopulateDatas
import com.openclassrooms.realestatemanager.datas.model.*

class FakeDatas {

    companion object {
        val propertySold =
            Property(
                4,
                4,
                "1075 Victory Blvd, Staten Island, NY 10314",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                2,
                3500000,
                150.00,
                2,
                1,
                1,
                "2022-01-01",
                "2022-02-01"
            )


        val fakePropertiesCompletes = listOf(
            PropertyWithProximity(
                PrepopulateDatas.preProperties[0],
                listOf(PrepopulateDatas.preProximities[0],PrepopulateDatas.preProximities[1]),
                PrepopulateDatas.preTypes[0],
                mutableListOf(PrepopulateDatas.prePhotos[0], PrepopulateDatas.prePhotos[1], PrepopulateDatas.prePhotos[2]),
                PrepopulateDatas.preAgents[0]
            ),
            PropertyWithProximity(
                PrepopulateDatas.preProperties[1],
                listOf(PrepopulateDatas.preProximities[2],PrepopulateDatas.preProximities[3]),
                PrepopulateDatas.preTypes[1],
                mutableListOf(PrepopulateDatas.prePhotos[0]),
                PrepopulateDatas.preAgents[1]
            )

        )


    }
    }