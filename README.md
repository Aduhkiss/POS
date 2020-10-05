# The Cloudy Co. POS

Created by Atticus Zambrana

## Manager Overrides

Manager Override codes are produced and given by The Cloudy Co.

This program uses The Cloudy Co. Supervisor Override API to check Manager overrides

## Module Codes

* 6667 - Change balance using Customer Relations (Requires Manager Override)
* 2001 - Refresh UPC's from backend
* VOID - Remove an item from the current transaction
* VOIDTOTAL - Completely void the entire transaction (Requires Manager override if balance larger then $50.00)

## Tender Codes

Codes used to accept tender

* CASH - Accept Cash Money
* ICANMAKEITRIGHT - Used when giving items for free (Requires Manager Override if balance is larger then $20.00)