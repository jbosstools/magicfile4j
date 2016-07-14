# magicfile4j
A java implementation of the linux 'magic file' reader, which backs the linux 'file' command. 

## What works?

### Offsets
- Numeric offsets (decimal, octal, and hex)
- relative offsets (decimal, octal, and hex)
- dereferenced offsets 
- dereferenced offsets read as specific sizes
- defererenced offsets with modifiers.  ex: (9.b+19)
- dereferenced offsets relative to previous level  &(9.b+19)

### Data types
- byte, ubyte
- short, ushort, beshort, leshort, ubeshort, uleshort
- long, ulong, belong, lelong, ubelong, ulelong
- float, befloat, lefloat
- double, bedouble, ledouble
- quad, bequad, lequad
- string  (Partially complete, does not respect flags!)
- pstring 

#### Data Type Masks 
The '&' mask is currently supported and functional. ex:  belong&0xFFFF

### Tests / Comparisons
- '=', '<', '>', '!', '&', '^', '~'
- Support for '~' is uncertain. Behavior seems to match that of the 'file' command, but conflicts with docs


## What doesn't work yet / stubbed out
### Data types
- date, bedate, ledate
- qdate, beqdate, leqdate
- ldate, beldate, leldate
- qldate, beqldate, leqldate
- qwdate, beqwdate, leqwdate
- leid3, beid3
- bestring16, lestring16
- melong, medate, meldate
- indirect, regex, search, default, clear


# References
The 'magic' command and file format:  http://linux.die.net/man/5/magic
The 'file' command:  http://linux.die.net/man/1/file

