Alarm Generator
===============

The Alarm Generator is a GFS proprietary program that can read a Rockwell Software Studio 5000 L5X program and convert it into an alarm file that can be imported into Factory Talk View Studio. Alarm Generator will read the tag descriptions in the Studio 5000 project and look for special markers in the comments, called _meta comments_.

Usage
-----

The Alarm Generator can be used in one of two ways, either GUI or CLI.

To use the GUI variant, start Alarm Generator with no command line arguments. A GUI will be displayed. Select a valid Studio 5000 program saved in L5X format for the input file and select a new or existing file for output. The output file will be overwritten after a confirmation dialog is displayed. Information will be displayed in the big info box in the middle. Press the _Generate_ button to generate the alarms file.

The CLI variant of the program requires two command line arguments. The first argument must be the name of a valid Studio 5000 program saved in L5X format. The second argument must be the name of an output file. The output file will be overwritten without confirmation. The CLI variant is suitable for use in batch files.

Making Alarms
-------------

To make an alarm, a tag must have the `(* alarms *)` meta comment associated with it. The alarms meta comment can be placed on a bit, DINT, or even on an AOI. If placed on a DINT or AOI, all sub DINTs, and bits are marked as alarm bits.

Alarm bits must have comments that start with a special single letter prefix:

* `f` - Mark the alarm as a fault
* `w` - Mark the alarm as a warning
* `s` - Mark the alarm as a spare
* `x` - Mark the alarm as custom

Each prefix selects a unique alarm class. Every alarm in each class as different default properties, such as text and background color. Messages with an invalid prefix will generate warnings, and no alarm message will be created. Any of the properties can be overriden on a per alarm basis with additional meta comments. The single letter prefix, along with any meta comments, will be stripped from the alarm message, including any resulting leading and trailing white space. As an example:

    f  This is (*backcolor=#00FF00*) a green fault

The above comment will generate the following fault message:

    This is  a green fault

Notice that there are two spaces between 'is' and 'a'. This is because the meta comment was removed and only leading and trailing white space is trimmed. The prefix f and the resulting white space in the beginning was stripped. The background color of the message will be green, instead of the default of red for faults. For another example:

    f(*backcolor=#00FF00*) This is a green fault

yields

    This is a green fault

and

    fThis is a normal fault

yields

    This is a normal fault

Alarm comments and meta comments are inherited when using AOIs. The `Alarms` AOI, for example, has a `(* alarms *)` meta comment on its `iAlarms` input parameter. When writing logic with coils that go to, say, `iAlarms.0`, a comment can be placed on that bit. It is unnecessary to mark `iAlarms` with a meta comment because it is inherited from the AOI.

When using AOIs that have their own alarm messages, all of the alarm meta comments can be done in the AOI editor. When creating an instance of an AOI, add a description to the AOI tag. Any non meta comments are concatenated from the AOI description, DINT description, and bit description to form the alarm message.

As an example, lets say a few GFSL_VFD_F800 instances are in the program. When generating alarms, the alarm descriptions, meta comments, and alarm class will be pulled from the AOI. Each generated alarm will then be prefixed with the AOI comment.

Comment concatenation also works with just bare DINTs. Any DINT in the program can be flagged with the `(* alarms *)` meta comment. All bits in the DINT will then become alarms and will be optionally prefixed with any text that that is in the DINT comment but not part of the meta comment.

Meta Comments
-------------

Alarm Generator reads all of its information from tag comments from Studio 5000. All meta comments must be enclosed in a set of parentheses and asterisks, as seen in the below example.

    (* alarms *)
    
The meta comments follows a simple key and value syntax. Each key and value pair must have an equals sign between them. Any number of keys or key value pairs are permitted in a meta comments. Spaces are optional, but there must be at least 1 space between key/value pairs. Quotes can be used for any key or value that contains spaces. Multiple meta comments in a single comment is also permitted. All of the below meta comments are valid

    This is a comment
    This (* key1 = value1 *) is a comment
    This is a comment (*key1=value1*)
    This (* flag1 flag2 key1=value1 key2 = value2 *) is (* "key 3" = "value 3" *) a comment

The below meta comments are invalid.

    Missing close parenthese star marker (* key1 key2
    Missing space (* key = value1key1 = value2 *)

The below meta comments are supported:

* `alarmnum = number` - Set the starting alarm number. This meta tag is only used in program or controller descriptions. The alarms in each program will be generated in ascending alarm number order.

* `alarms` - Marks this tag and all sub tags as alarm bits.
* `backcolor = #RRGGBB` - Overrides the background color of a particular alarm.
* `forecolor = #RRGGBB` - Overrides the foreground color of a particular alarm.
* `audio = bool` - Override the default `audio` message value.
* `display = bool` - Override the default `display` message value.
* `print = bool` - Override the default `print` message value.
* `message-to-tag = bool` - Override the default `message-to-tag`.
