package kiwi.orbit.compose.catalog.screens

import kotlinx.serialization.Serializable

@Serializable
class Response(
    val itinerary: Itinerary
) {
    @Serializable
    class Itinerary(
        val sectors: List<Sector>,
    )
    @Serializable
    class Sector(
        val segment_groups: List<SegmentGroup>,
    )
    @Serializable
    class SegmentGroup(
        val segments: List<Segment>
    )
    @Serializable
    class Segment(
        val additional_info_groups: List<AdditionalInfoGroup>,
        val arrival: Destination,
        val cabin_class: CabinClass,
        val carrier: Carrier,
        val connection_number: ConnectionNumber,
        val departure: Destination,
        val duration: Duration,
        val hidden: Hidden,
        val vehicle_type: VehicleType,
    )
    @Serializable
    class AdditionalInfoGroup(
        val items: List<Item>,
        val title: String,
    ) {
        @Serializable
        class Item(
            val icon: String,
            val name: String,
            val value: String,
        )
    }
    @Serializable
    class Destination(
        val city: String,
        val code: String,
        val datetime_local: String,
        val station: String,
        val warning: Boolean,
    )
    @Serializable
    class CabinClass(
        val value: String,
    )
    @Serializable
    class Carrier(
        val carrier_icon_id: String,
        val id: String,
        val name: String,
    )
    @Serializable
    class ConnectionNumber(
        val value: String,
    )
    @Serializable
    class Duration(
        val value: String,
    )
    @Serializable
    class Hidden(
        val is_segment_hidden: Boolean,
    )
    @Serializable
    class VehicleType(
        val value: String,
    )
}

// region json
val json = """
    {
      "banners": [
        {
          "description": "Please check your itinerary, including layovers, for <a data-smartfaqid=\"236\" href=\"https://kiwi.com/en/help/search/article/236\">travel restrictions</a> prior to booking. A displayed itinerary is not confirmation of your eligibility to travel.",
          "position": "landing",
          "title": "COVID-19 information",
          "type": "info"
        }
      ],
      "billing_details": {
        "is_latin_only": false,
        "is_mandatory": false
      },
      "health_declaration_required": false,
      "itinerary": {
        "sectors": [
          {
            "segment_groups": [
              {
                "badges": [],
                "segment_group_type": "default",
                "segments": [
                  {
                    "additional_info_groups": [
                      {
                        "items": [
                          {
                            "icon": "info",
                            "name": "Carrier",
                            "value": "Wizz Air"
                          }
                        ],
                        "title": "Connection info"
                      }
                    ],
                    "arrival": {
                      "city": "London",
                      "code": "LTN",
                      "datetime_local": "2021-11-24T18:55:00",
                      "station": "Luton",
                      "warning": false
                    },
                    "cabin_class": {
                      "value": "Economy"
                    },
                    "carrier": {
                      "carrier_icon_id": "W6",
                      "id": "W6",
                      "name": "Wizz Air"
                    },
                    "connection_number": {
                      "value": "W6 2879"
                    },
                    "departure": {
                      "city": "Vienna",
                      "code": "VIE",
                      "datetime_local": "2021-11-24T17:30:00",
                      "station": "Vienna International Airport",
                      "warning": false
                    },
                    "duration": {
                      "value": "PT2H25M"
                    },
                    "hidden": {
                      "is_segment_hidden": false,
                      "reason": null
                    },
                    "layover": {
                      "badges": []
                    },
                    "vehicle_type": {
                      "value": "airplane"
                    }
                  }
                ],
                "title": ""
              }
            ],
            "title": "Vienna -> London"
          }
        ]
      },
      "passengers": {
        "age_bookability_category_thresholds": {
          "adult": 14,
          "child": 2
        },
        "age_category_thresholds": {
          "adult": 14,
          "child": 2
        },
        "age_pricing_category_thresholds": {
          "adult": 14,
          "child": 2
        }
      }
    }
"""
// endregion json
