

# Endpoints
Here you can better understand the Rest API for Liberty Web and utilize them to create your own frontend or other software.

## Punishment Endpoint
This endpoint returns punishments by type using a pagination system.
You can access it using the `punishments/{punishmentType}/{pageNumber}` url.

The `{punishmentType}` variable is what type of punishment you want to query. The types are:
- ban
- mute
- warn
- kick

The `{pageNumber}` is just the number of the page you want to view.
The endpoint responds in some JSON that looks like this:

```json
{
   "morePages":false,
   "punishments":[
      {
         "victimUuid":"960fdc33-e2d3-4b1e-9957-9519659f9784",
         "victimUsername":"computer",
         "operatorUuid":"00000000-0000-0000-0000-000000000000",
         "operatorUsername":"Console",
         "reason":"flying 5d",
         "active":false,
         "start":1693683950,
         "end":0,
         "label":"Permanent"
      }
   ]
}
```

The `morePages` boolean signals if there are any more pages. For example if you query `punishments/ban/3` and the `pageNumber` is true there is at least one more page available, if false you have reached the end.

`punishments` is a JSON array of objects of punishments. Most of the object's values are self explanatory other then the `end`, if `end` is 0 that means that the punishment will never expire. If the `operatorUuid` is `00000000-0000-0000-0000-000000000000` that means the operator is the console.

## Statistics Endpoint
This endpoint tells you the amount of total punishments there are.
You can access it with the url `stats/{type}`. The type can be one of the following:
- all (returns the total sum of all punishments)
- ban
- mute
- warn
- kick

The endpoint responds in very short JSON
```json
{"stats":1}
```
`stats` of course being the number of punishments queried.
