package com.openclassrooms.realestatemanager.datas.database

import com.openclassrooms.realestatemanager.datas.model.*

class PrepopulateDatas {

    companion object {
        val preProximities = listOf(
            Proximity(1, "School", "icon_proximity_school", "proximity_school"),
            Proximity(2, "Stores", "icon_proximity_stores", "proximity_stores"),
            Proximity(3, "Public transports", "icon_proximity_transports", "proximity_transports"),
            Proximity(4, "Park", "icon_proximity_park", "proximity_park")
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
            TypeOfProperty(1, "Condo"),
            TypeOfProperty(2, "Loft"),
            TypeOfProperty(3, "Mansion"),
            TypeOfProperty(4, "Single Family House")
        )

        val preAgents = listOf(
            Agent(1, "Mike Money"),
            Agent(2, "Melissa BigDollars")
        )

        val prePhotos = listOf(
            ImageRoom(1, 1, "flat1", "Living-room"),
            ImageRoom(2, 2, "flat2", "A room"),
            ImageRoom(3, 1, "flat2", "Another Room"),
            ImageRoom(4, 3, "flat3", "1st Room"),
            ImageRoom(5, 4, "flat4", "1st Room"),
            ImageRoom(6, 5, "flat5", "1st Room"),
            ImageRoom(7, 6, "flat6", "1st Room"),
            ImageRoom(8, 3, "flat4", "2nd Room"),
            ImageRoom(9, 4, "flat5", "2nd Room"),
            ImageRoom(10, 5, "flat6", "2nd Room"),
            ImageRoom(11, 6, "flat7", "2nd Room"),
            ImageRoom(12, 1, "flat8", "Another Room"),
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
                null
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
                null
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
                null
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
                null
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
                null
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
                null
            )
        )


    }
}