# PvP Toggle

A lightweight Hytale server plugin that allows players to toggle their PvP status on or off.

## Requirements

- PvP must be enabled in the world configuration. If the world has PvP disabled at the world level, this plugin will have no effect.

## Features

- **Per-player PvP toggle** - Each player can independently enable or disable their PvP status
- **Combat protection** - Players cannot toggle PvP while actively in combat
- **Toggle cooldown** - Configurable cooldown between PvP state changes to prevent abuse
- **Persistent state** - PvP status is saved across server restarts
- **Admin controls** - Server administrators can view and modify plugin settings in-game

## How It Works

When a player has PvP disabled:
- They cannot damage other players
- They cannot be damaged by other players

If either the attacker or the target has PvP disabled, no damage is dealt.

The combat timer prevents players from disabling PvP mid-fight. After engaging in PvP combat, players must wait for the combat timer to expire before they can change their PvP status.

## Commands

### Player Commands

| Command | Description |
|---------|-------------|
| `/pvp on` | Enable PvP for yourself |
| `/pvp off` | Disable PvP for yourself |
| `/pvp status` | Check your current PvP status, combat timer, and cooldown |

### Admin Commands

| Command | Description |
|---------|-------------|
| `/pvp admin config` | Display current plugin configuration |
| `/pvp admin set <key> <value>` | Modify a configuration value |

#### Configuration Keys

| Key | Type | Description |
|-----|------|-------------|
| `combattimer` | seconds | How long after PvP damage before you can toggle (0 to disable) |
| `cooldown` | seconds | How long between PvP toggles (0 to disable) |
| `default` | true/false | Default PvP state for new players |
| `persist` | true/false | Save PvP state across server restarts |

Boolean values accept: `true`, `false`, `yes`, `no`, `on`, `off`, `1`, `0`

## Default Configuration

| Setting | Default |
|---------|---------|
| Combat Timer | 10 seconds |
| Toggle Cooldown | 5 seconds |
| Default PvP State | Enabled |
| Data Persistence | Enabled |

## Permissions

- All players can use `/pvp on`, `/pvp off`, and `/pvp status`
- Admin commands require Creative mode permissions

## Installation

1. Build the plugin JAR
2. Place in your server's plugins folder
3. Restart the server

## License

MIT
