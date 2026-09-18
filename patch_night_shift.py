import sys

file_path = "app/src/main/java/com/example/ui/screens/OutletChecklistScreen.kt"
with open(file_path, "r") as f:
    text = f.read()

# Replace SHIFTS
text = text.replace('val SHIFTS = listOf("Opening", "Closing")', 'val SHIFTS = listOf("Opening", "Closing", "Night")')

new_function = """fun getTasksForOutletAndShift(outlet: String, shift: String): List<OutletChecklistItem> {
    val tasks = mutableListOf<String>()
    
    // Generic tasks for all outlets
    when (shift) {
        "Opening" -> {
            tasks.addAll(listOf(
                "Keys collected and outlet unlocked securely",
                "Lights, AC, and music system turned on",
                "POS system booted up and daily float verified",
                "Staff briefing conducted and grooming standards checked",
                "Tables wiped, set up, and aligned properly",
                "Condiments, salt/pepper, and sugar caddies refilled",
                "Allergen charts and menus clean and presentable"
            ))
        }
        "Closing" -> {
            tasks.addAll(listOf(
                "Final billings settled and POS batched out",
                "Cash float secured and deposited to front office/accounts",
                "Tables cleared, wiped, and chairs arranged",
                "Perishables returned to main kitchen or stored properly",
                "Lights, AC, and music system turned off",
                "Outlet locked and keys handed over to security"
            ))
        }
        "Night" -> {
            tasks.addAll(listOf(
                "Verify night staff attendance and brief on VIPs in-house",
                "Ensure all access doors and non-24h areas are securely locked",
                "Perform midnight POS system audit and rollover",
                "Deep cleaning of front-of-house floors and upholstery",
                "Restock all supplies for morning opening shift",
                "Check all chillers and freezers are at correct temperatures",
                "Prepare early bird breakfast boxes for early departures"
            ))
        }
    }

    // Specific tasks
    when (outlet) {
        "Coffee Shop" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Coffee machines calibrated and tested (espresso shot check)",
                        "Display fridge stocked with fresh pastries & sandwiches",
                        "Buffet counters heated/cooled and ready for service",
                        "Juice dispensers filled and chilled"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Coffee machines backflushed and steam wands cleaned",
                        "Display fridge items wrapped and labelled for expiry",
                        "Buffet counters cleaned and sanitized"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Run auto-clean cycles on all espresso machines",
                        "Sanitize juicers and blenders thoroughly",
                        "Stock up on takeaway cups, lids, and stirrers",
                        "Ensure pastry cases are clean and ready for morning load"
                    ))
                }
            }
        }
        "Banquet" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Function sheet (BEO) reviewed with the team",
                        "A/V equipment (mics, projectors) tested",
                        "Stage, podium, and seating arranged as per BEO",
                        "Chafing dishes placed with fuel cells ready"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Leftover food properly discarded or returned",
                        "Linens counted and sent to laundry",
                        "A/V equipment switched off and secured",
                        "Hall cleared of all event debris"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Set up tables and chairs for next morning's breakfast/conference",
                        "Polish all cutlery and glassware for next day's events",
                        "Check banquet hall AC and lighting automation schedules",
                        "Deep clean carpets in pre-function area"
                    ))
                }
            }
        }
        "Speciality Restaurant" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Reservations list checked and tables assigned",
                        "Wine chillers temperature checked",
                        "Special chef's specials board updated",
                        "Fine dining cutlery and glassware polished"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Open wine bottles vacuum-sealed and dated",
                        "Linen napkins separated for laundry",
                        "Inventory of high-value items (cigars, premium liquor) verified",
                        "Next day's reservations reviewed"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Deep clean the host/maitre d' station",
                        "Update the digital wine list and POS with 86'd items",
                        "Perform weekly silver polishing (if scheduled)",
                        "Secure all fine dining silverware in lockboxes"
                    ))
                }
            }
        }
        "Tea/Coffee Lounge & Patisserie" -> {
            when (shift) {
                "Opening" -> {
                    tasks.addAll(listOf(
                        "Cake display vitrine temperature checked (2-4°C)",
                        "Fresh bakes arrayed with correct FSSAI tags",
                        "Tea selection boxes refilled and organized",
                        "Takeaway packaging stocked at the counter"
                    ))
                }
                "Closing" -> {
                    tasks.addAll(listOf(
                        "Unsold perishable pastries discarded/logged as wastage",
                        "Cake display vitrine defrosted/cleaned",
                        "Coffee/Tea stations thoroughly sanitized",
                        "Takeaway materials restocked for next day"
                    ))
                }
                "Night" -> {
                    tasks.addAll(listOf(
                        "Clean the cake display vitrine glass inside and out",
                        "Receive and date early morning bakery deliveries",
                        "Stock and organize retail tea/coffee beans displays",
                        "Verify temperatures of all dessert chillers"
                    ))
                }
            }
        }
    }
    
    return tasks.map { OutletChecklistItem(task = it) }
}"""

start_idx = text.find("fun getTasksForOutletAndShift(")
end_idx = text.find("\n\nfun sendWhatsAppReport(")

if start_idx != -1 and end_idx != -1:
    text = text[:start_idx] + new_function + text[end_idx:]
    with open(file_path, "w") as f:
        f.write(text)
    print("Success")
else:
    print("Failed to replace getTasksForOutletAndShift")
