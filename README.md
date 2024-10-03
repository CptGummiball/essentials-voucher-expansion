# Essentials X Voucher Expansion
![Spigot](https://img.shields.io/badge/Spigot-1.20--1.21.1-yellow.svg)
![Paper](https://img.shields.io/badge/PaperMC-1.20--1.21.1-blue.svg)
![Version](https://img.shields.io/badge/Version-1.0-lightgray.svg)
![MIT License](https://img.shields.io/badge/License-MIT-green.svg)

EXVoucherExpansion is a Spigot plugin designed to give players kits based on specific keywords typed in the chat. This plugin works as an extension of EssentialsX, utilizing its `/kit` command to distribute kits dynamically. The plugin is fully customizable through configuration files, allowing for unlimited keywords, kit lists, and kit limits.

## Features

- **Keyword-based kit distribution**: Players receive kits when they type predefined keywords in the chat.
- **EssentialsX integration**: The plugin leverages EssentialsX's `/kit` command to grant kits to players.
- **Per-player and global kit limits**: Define how many times each player can receive specific kits, and set global limits for kits.
- **Customizable messages**: Modify the messages displayed to players when they receive kits or when no kits are available through a `lang.yml` file.
- **Unlimited keywords**: Add as many keywords and corresponding kits as needed.

## Installation

1. Download the latest version of the plugin.
2. Place the `.jar` file in your server's `plugins` folder.
3. Start your server to generate the default configuration files.
4. Configure the plugin by editing the `config.yml` and `lang.yml` files in the `plugins/EXVoucherExpansion` directory.
5. Restart your server for the changes to take effect.

## Configuration

### `config.yml`

This file defines the keywords, kit options, and limits for each kit. You can configure unlimited keywords and assign kits with specific limits.

```yaml
# You can set unlimited Keywords
keyword1:
  # Limits - 0 = unlimited
  perPlayerLimit: 1      # Number of times a player can receive a specific kit
  GlobalLimit: 0         # Global limit for how many times a kit can be distributed
  # Set Kits as List with Kit Name and a Limit separated by a comma
  Kits:
    - "kit1", 20         # Kit name and its global limit
    - "kit2", 1
    - "kit3", 0          # 0 means unlimited usage

# Add more keywords as needed
# keyword2:
  # perPlayerLimit: 0
  # GlobalLimit: 5
  # Kits:
    # - "starter", 10
    # - "basic", 5
    # - "vip", 0
````
### lang.yml
This file allows for customization of the plugin's messages, making it easy to translate or personalize in-game notifications.

```yaml
getkit: "You have been given the kit: "
nokit: "No available kits for the keyword: "
````
## How It Works
1. Players type a keyword in the chat.
2. If the keyword matches one of the predefined keywords in `config.yml`, the plugin checks the player's kit limits.
3. The player receives a kit from the defined list, respecting per-player and global limits.
4. If no kits are available for that keyword (due to limits), the player is notified with a custom message.

## Commands
The plugin relies on EssentialsX /kit command to give players their kits. No additional commands are introduced by this plugin.

## Permissions
EXVoucherExpansion uses the same permissions as EssentialsX for kit access. Ensure players have the necessary permissions for kits.

Example permission for kits:

```yaml
essentials.kits.<kitname>
````

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
