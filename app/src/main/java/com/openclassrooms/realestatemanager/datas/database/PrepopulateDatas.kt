package com.openclassrooms.realestatemanager.datas.database

import com.openclassrooms.realestatemanager.datas.model.*

class PrepopulateDatas {

    companion object {
        val preProximities = listOf(
            Proximity(1, "School", "icon_proximity_school", "proximity_school",1000L),
            Proximity(2, "Stores", "icon_proximity_stores", "proximity_stores",1000L),
            Proximity(3, "Public transports", "icon_proximity_transports", "proximity_transports",1000L),
            Proximity(4, "Park", "icon_proximity_park", "proximity_park",1000L)
        )

        val preCrossRef = listOf(
            PropertyProximityCrossRef(1, 1),
            PropertyProximityCrossRef(1, 2),
            PropertyProximityCrossRef(1, 3),
            PropertyProximityCrossRef(1, 4),
            PropertyProximityCrossRef(2, 1),
            PropertyProximityCrossRef(2, 3),
            PropertyProximityCrossRef(3, 1),
            PropertyProximityCrossRef(4, 2),
            PropertyProximityCrossRef(5, 3),
            PropertyProximityCrossRef(6, 4),
        )

        val preTypes = listOf<TypeOfProperty>(
            TypeOfProperty(1, "Condo",1000L),
            TypeOfProperty(2, "Loft",1000L),
            TypeOfProperty(3, "Mansion",1000L),
            TypeOfProperty(4, "Single Family House",1000L)
        )

        val preAgents = listOf(
            Agent(1, "Mike Money",1000L),
            Agent(2, "Melissa BigDollars",1000L)
        )

        val prePhotos = listOf(
            ImageRoom(1, 1, "flat1", "Living-room",1000L),
            ImageRoom(2, 2, "flat2", "A room",1000L),
            ImageRoom(3, 1, "flat2", "Another Room",1000L),
            ImageRoom(4, 3, "flat3", "1st Room",1000L),
            ImageRoom(5, 4, "flat4", "1st Room",1000L),
            ImageRoom(6, 5, "flat5", "1st Room",1000L),
            ImageRoom(7, 6, "flat6", "1st Room",1000L),
            ImageRoom(8, 3, "flat4", "2nd Room",1000L),
            ImageRoom(9, 4, "flat5", "2nd Room",1000L),
            ImageRoom(10, 5, "flat6", "2nd Room",1000L),
            ImageRoom(11, 6, "flat7", "2nd Room",1000L),
            ImageRoom(12, 1, "flat8", "Another Room",1000L),
        )

        val preProperties = listOf(
            Property(
                1,
                1,
                "386 Woolley Ave, Staten Island, NY 10314",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                1,
                1500000,
                300.00,
                6,
                4,
                2,
                "2022-01-01",
                null,
                1000L
            ),
            Property(
                2,
                2,
                "2071 Victory Blvd, Staten Island, NY 10314",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                1,
                999000,
                100.00,
                4,
                3,
                1,
                "2022-01-01",
                null,
                1000L
            ),
            Property(
                3,
                2,
                "2075 Victory Blvd, Staten Island, NY 10314",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                1,
                100000,
                30.00,
                2,
                1,
                1,
                "2022-01-01",
                null,
                1000L
            ),

            Property(
                4,
                2,
                "550 Monroe Ave, Elizabeth",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                1,
                1900000,
                130.00,
                7,
                4,
                2,
                "2022-01-30",
                null,
                1000L
            ),
            Property(
                5,
                3,
                "428-410 Clinton Pl, Newark",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                2,
                1600000,
                140.00,
                8,
                5,
                2,
                "2022-02-02",
                null,
                1000L
            ),
            Property(
                6,
                1,
                "171-0-171-98 67th Ave",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                2,
                2500000,
                100.00,
                5,
                3,
                2,
                "2022-02-02",
                null,
                1000L
            )
        )


    }
}