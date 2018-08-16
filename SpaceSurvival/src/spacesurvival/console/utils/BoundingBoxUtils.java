/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console.utils;

/**
 *
 * @author bowen
 */
public interface BoundingBoxUtils {
    
    public static boolean isCharBoxBound(char c) {
        return (c >= 0x2500 && c <= 0x257F);
    }
    public static boolean isCharBlockElement(char c) {
        return (c >= 0x2580 && c <= 0x259F);
    }
    
    public static boolean hasSingleLineLeft(char c) {
        switch (c) {
            case '┤':
            case '╢':
            case '╖':
            case '╜':
            case '┐':
            case '┴':
            case '┬':
            case '─':
            case '┼':
            case '╨':
            case '╥':
            case '╫':
            case '┘':
                return true;
            default :
                return false;
        }
    }
    public static boolean hasSingleLineRight(char c) {
        switch (c) {
            case '└':
            case '┴':
            case '┬':
            case '├':
            case '─':
            case '┼':
            case '╟':
            case '╨':
            case '╥':
            case '╙':
            case '╓':
            case '╫':
            case '┌':
                return true;
            default :
                return false;
        }
    }
    public static boolean hasSingleLineUp(char c) {
        switch (c) {
            case '│':
            case '┤':
            case '╡':
            case '╛':
            case '└':
            case '┴':
            case '├':
            case '┼':
            case '╞':
            case '╧':
            case '╘':
            case '╪':
            case '┘':
                return true;
            default :
                return false;
        }
    }
    public static boolean hasSingleLineDown(char c) {
        switch (c) {
            case '│':
            case '┤':
            case '╡':
            case '╕':
            case '┐':
            case '┬':
            case '├':
            case '┼':
            case '╞':
            case '╤':
            case '╒':
            case '╪':
            case '┌':
                return true;
            default :
                return false;
        }
    }
    
    public static char addSingleDownRight(char c) {
        if (!isCharBoxBound(c)) {
            return '┌';
        } else {
            switch (c) {
                case '│':
                    return '├';
                case '┤':
                    return '┼';
                case '╡':
                    return '╪';
                case '╢':
                    return '╫';
                case '╖':
                    return '╥';
                case '╕':
                    return '╤';
                case '╣':
                    return '╬';
                case '║':
                    return '╟';
                case '╗':
                    return '╦';
                case '╝':
                    return '╬';
                case '╜':
                    return '╫';
                case '╛':
                    return '╪';
                case '┐':
                    return '┬';
                case '└':
                    return '├';
                case '┴':
                    return '┼';
                case '┬':
                    return c;
                case '├':
                    return c;
                case '─':
                    return '┬';
                case '┼':
                    return c;
                case '╞':
                    return c;
                case '╟':
                    return c;
                case '╚':
                    return '╠';
                case '╔':
                    return c;
                case '╩':
                    return '╬';
                case '╦':
                    return c;
                case '╠':
                    return c;
                case '═':
                    return '╤';
                case '╬':
                    return c;
                case '╧':
                    return '╪';
                case '╨':
                    return '╫';
                case '╤':
                    return c;
                case '╥':
                    return c;
                case '╙':
                    return '╟';
                case '╘':
                    return '╞';
                case '╒':
                    return c;
                case '╓':
                    return c;
                case '╫':
                    return c;
                case '╪':
                    return c;
                case '┘':
                    return '┼';
                case '┌':
                    return c;
                default :
                    return c;
            }
        }
    }
    public static char addSingleDownLeft(char c) {
        if (!isCharBoxBound(c)) {
            return '┐';
        } else {
            switch (c) {
                case '│':
                    return '┤';
                case '┤':
                    return c;
                case '╡':
                    return c;
                case '╢':
                    return c;
                case '╖':
                    return '╢';
                case '╕':
                    return c;
                case '╣':
                    return c;
                case '║':
                    return '╢';
                case '╗':
                    return c;
                case '╝':
                    return '╣';
                case '╜':
                    return '╢';
                case '╛':
                    return '╡';
                case '┐':
                    return c;
                case '└':
                    return '┼';
                case '┴':
                    return '┼';
                case '┬':
                    return c;
                case '├':
                    return '┼';
                case '─':
                    return '┬';
                case '┼':
                    return c;
                case '╞':
                    return '╪';
                case '╟':
                    return '╫';
                case '╚':
                    return '╬';
                case '╔':
                    return '╦';
                case '╩':
                    return '╬';
                case '╦':
                    return c;
                case '╠':
                    return '╬';
                case '═':
                    return '╤';
                case '╬':
                    return c;
                case '╧':
                    return '╪';
                case '╨':
                    return '╫';
                case '╤':
                    return c;
                case '╥':
                    return c;
                case '╙':
                    return '╫';
                case '╘':
                    return '╪';
                case '╒':
                    return '╤';
                case '╓':
                    return '╥';
                case '╫':
                    return c;
                case '╪':
                    return c;
                case '┘':
                    return '┤';
                case '┌':
                    return '┬';
                default :
                    return c;
            }
        }
    }
    public static char addSingleUpRight(char c) {
        if (!isCharBoxBound(c)) {
            return '└';
        } else {
            switch (c) {
                case '│':
                    return '├';
                case '┤':
                    return '┼';
                case '╡':
                    return '╪';
                case '╢':
                    return '╫';
                case '╖':
                    return '╫';
                case '╕':
                    return '╪';
                case '╣':
                    return '╬';
                case '║':
                    return '╟';
                case '╗':
                    return '╬';
                case '╝':
                    return '╩';
                case '╜':
                    return '╨';
                case '╛':
                    return '╧';
                case '┐':
                    return '┼';
                case '└':
                    return c;
                case '┴':
                    return c;
                case '┬':
                    return '┼';
                case '├':
                    return c;
                case '─':
                    return '┴';
                case '┼':
                    return c;
                case '╞':
                    return c;
                case '╟':
                    return c;
                case '╚':
                    return c;
                case '╔':
                    return '╠';
                case '╩':
                    return c;
                case '╦':
                    return '╬';
                case '╠':
                    return c;
                case '═':
                    return '╧';
                case '╬':
                    return c;
                case '╧':
                    return c;
                case '╨':
                    return c;
                case '╤':
                    return '╪';
                case '╥':
                    return '╫';
                case '╙':
                    return c;
                case '╘':
                    return c;
                case '╒':
                    return '╞';
                case '╓':
                    return '╟';
                case '╫':
                    return c;
                case '╪':
                    return c;
                case '┘':
                    return '┴';
                case '┌':
                    return '├';
                default :
                    return c;
            }
        }
    }
    public static char addSingleUpLeft(char c) {
        if (!isCharBoxBound(c)) {
            return '┘';
        } else {
            switch (c) {
                case '│':
                    return '┤';
                case '┤':
                    return c;
                case '╡':
                    return c;
                case '╢':
                    return c;
                case '╖':
                    return '╢';
                case '╕':
                    return '╡';
                case '╣':
                    return c;
                case '║':
                    return '╢';
                case '╗':
                    return '╣';
                case '╝':
                    return c;
                case '╜':
                    return c;
                case '╛':
                    return c;
                case '┐':
                    return '┤';
                case '└':
                    return '┴';
                case '┴':
                    return c;
                case '┬':
                    return '┼';
                case '├':
                    return '┼';
                case '─':
                    return '┴';
                case '┼':
                    return c;
                case '╞':
                    return '╪';
                case '╟':
                    return '╫';
                case '╚':
                    return '╩';
                case '╔':
                    return '╬';
                case '╩':
                    return c;
                case '╦':
                    return '╬';
                case '╠':
                    return '╬';
                case '═':
                    return '╩';
                case '╬':
                    return c;
                case '╧':
                    return c;
                case '╨':
                    return c;
                case '╤':
                    return '╪';
                case '╥':
                    return '╫';
                case '╙':
                    return '╨';
                case '╘':
                    return '╧';
                case '╒':
                    return '╪';
                case '╓':
                    return '╫';
                case '╫':
                    return c;
                case '╪':
                    return c;
                case '┘':
                    return c;
                case '┌':
                    return '┼';
                default :
                    return c;
            }
        }
    }
    
    public static char addSingleHorizontal(char c) {
        if (!isCharBoxBound(c)) {
            return '─';
        } else {
            switch (c) {
                case '│':
                    return '┼';
                case '┤':
                    return '┼';
                case '╡':
                    return '╪';
                case '╢':
                    return '╫';
                case '╖':
                    return '╥';
                case '╕':
                    return '╤';
                case '╣':
                    return '╬';
                case '║':
                    return '╫';
                case '╗':
                    return '╦';
                case '╝':
                    return '╩';
                case '╜':
                    return '╨';
                case '╛':
                    return '╧';
                case '┐':
                    return '┬';
                case '└':
                    return '┴';
                case '┴':
                    return '┼';
                case '┬':
                    return c;
                case '├':
                    return '┼';
                case '─':
                    return c;
                case '┼':
                    return c;
                case '╞':
                    return '╪';
                case '╟':
                    return '╫';
                case '╚':
                    return '╩';
                case '╔':
                    return '╦';
                case '╩':
                    return '╩';
                case '╦':
                    return '╦';
                case '╠':
                    return '╬';
                case '═':
                    return c;
                case '╬':
                    return c;
                case '╧':
                    return '╧';
                case '╨':
                    return '╨';
                case '╤':
                    return '╤';
                case '╥':
                    return '╥';
                case '╙':
                    return '╨';
                case '╘':
                    return '╧';
                case '╒':
                    return '╤';
                case '╓':
                    return '╥';
                case '╫':
                    return c;
                case '╪':
                    return c;
                case '┘':
                    return '┴';
                case '┌':
                    return '┬';
                default :
                    return c;
            }
        }
    }
    public static char addSingleVertical(char c) {
        if (!isCharBoxBound(c)) {
            return '│';
        } else {
            switch (c) {
                case '│':
                    return c;
                case '┤':
                    return c;
                case '╡':
                    return c;
                case '╢':
                    return c;
                case '╖':
                    return '╢';
                case '╕':
                    return '╡';
                case '╣':
                    return c;
                case '║':
                    return c;
                case '╗':
                    return '╣';
                case '╝':
                    return '╣';
                case '╜':
                    return '╢';
                case '╛':
                    return '╡';
                case '┐':
                    return '┤';
                case '└':
                    return '├';
                case '┴':
                    return '┼';
                case '┬':
                    return '┼';
                case '├':
                    return c;
                case '─':
                    return '┼';
                case '┼':
                    return c;
                case '╞':
                    return c;
                case '╟':
                    return c;
                case '╚':
                    return '╠';
                case '╔':
                    return '╠';
                case '╩':
                    return '╬';
                case '╦':
                    return '╬';
                case '╠':
                    return c;
                case '═':
                    return '╪';
                case '╬':
                    return c;
                case '╧':
                    return '╪';
                case '╨':
                    return '╫';
                case '╤':
                    return '╪';
                case '╥':
                    return '╫';
                case '╙':
                    return '╟';
                case '╘':
                    return '╞';
                case '╒':
                    return '╞';
                case '╓':
                    return '╟';
                case '╫':
                    return c;
                case '╪':
                    return c;
                case '┘':
                    return '┤';
                case '┌':
                    return '├';
                default :
                    return c;
            }
        }
    }
    
    public static boolean hasDoubleLineLeft(char c) {
        switch (c) {
            case '╡':
            case '╕':
            case '╣':
            case '╗':
            case '╝':
            case '╛':
            case '╩':
            case '╦':
            case '═':
            case '╬':
            case '╧':
            case '╤':
            case '╪':
                return true;
            default :
                return false;
        }
    }
    public static boolean hasDoubleLineRight(char c) {
        switch (c) {
            case '╞':
            case '╚':
            case '╔':
            case '╩':
            case '╦':
            case '╠':
            case '═':
            case '╬':
            case '╧':
            case '╤':
            case '╘':
            case '╒':
            case '╪':
                return true;
            default :
                return false;
        }
    }
    public static boolean hasDoubleLineUp(char c) {
        switch (c) {
            case '╢':
            case '╣':
            case '║':
            case '╝':
            case '╜':
            case '╟':
            case '╚':
            case '╩':
            case '╠':
            case '╬':
            case '╨':
            case '╙':
            case '╫':
                return true;
            default :
                return false;
        }
    }
    public static boolean hasDoubleLineDown(char c) {
        switch (c) {
            case '╢':
            case '╖':
            case '╣':
            case '║':
            case '╗':
            case '╟':
            case '╔':
            case '╦':
            case '╠':
            case '╬':
            case '╥':
            case '╓':
            case '╫':
                return true;
            default :
                return false;
        }
    }
    
    public static char addDoubleDownRight(char c) {
        if (!isCharBoxBound(c)) {
            return '╔';
        } else {
            switch (c) {
                case '│':
                case '║':
                case '└':
                case '├':
                case '╞':
                case '╟':
                case '╚':
                case '╙':
                case '╘':
                    return '╠';
                case '┤':
                case '╡':
                case '╢':
                case '╣':
                case '╝':
                case '╜':
                case '╛':
                case '┴':
                case '┼':
                case '╩':
                case '╧':
                case '╨':
                case '╫':
                case '╪':
                case '┘':
                    return '╬';
                case '╖':
                case '╕':
                case '╗':
                case '┐':
                case '┬':
                case '─':
                case '═':
                case '╤':
                case '╥':
                    return '╦';
                case '╒':
                case '╓':
                case '┌':
                    return '╔';
                case '╔':
                case '╦':
                case '╠':
                case '╬':
                default :
                    return c;
            }
        }
    }
    public static char addDoubleDownLeft(char c) {
        if (!isCharBoxBound(c)) {
            return '╗';
        } else {
            switch (c) {
                case '│':
                case '║':
                case '┤':
                case '╡':
                case '╢':
                case '╣':
                case '╝':
                case '╜':
                case '╛':
                case '┘':
                    return '╣';
                case '┴':
                case '┼':
                case '╩':
                case '╧':
                case '╨':
                case '╫':
                case '╪':
                case '╠':
                case '└':
                case '├':
                case '╞':
                case '╟':
                case '╚':
                case '╙':
                case '╘':
                    return '╬';
                case '╒':
                case '╓':
                case '┌':
                case '┬':
                case '─':
                case '═':
                case '╤':
                case '╥':
                case '╔':
                    return '╦';
                case '╖':
                case '╕':
                case '┐':
                    return '╗';
                case '╗':
                case '╦':
                case '╬':
                default :
                    return c;
            }
        }
    }
    public static char addDoubleUpRight(char c) {
        if (!isCharBoxBound(c)) {
            return '╚';
        } else {
            switch (c) {
                case '│':
                case '║':
                case '├':
                case '╞':
                case '╟':
                case '╒':
                case '╓':
                case '┌':
                case '╔':
                    return '╠';
                case '┬':
                case '╦':
                case '╤':
                case '╥':
                case '╗':
                case '╖':
                case '╕':
                case '┐':
                case '┤':
                case '╡':
                case '╢':
                case '╣':
                case '┼':
                case '╫':
                case '╪':
                    return '╬';
                case '╝':
                case '╜':
                case '╛':
                case '┘':
                case '─':
                case '═':
                case '╩':
                case '╧':
                case '╨':
                case '┴':
                    return '╩';
                case '╙':
                case '╘':
                case '└':
                    return '╚';
                case '╚':
                case '╠':
                case '╬':
                default :
                    return c;
            }
        }
    }
    public static char addDoubleUpLeft(char c) {
        if (!isCharBoxBound(c)) {
            return '╝';
        } else {
            switch (c) {
                case '│':
                case '║':
                case '╗':
                case '╖':
                case '╕':
                case '┐':
                case '┤':
                case '╡':
                case '╢':
                    return '╣';
                case '├':
                case '╞':
                case '╟':
                case '╠':
                case '╒':
                case '╓':
                case '┌':
                case '╔':
                case '┬':
                case '╦':
                case '╤':
                case '╥':
                case '┼':
                case '╫':
                case '╪':
                    return '╬';
                case '╙':
                case '╘':
                case '└':
                case '╚':
                case '─':
                case '═':
                case '╩':
                case '╧':
                case '╨':
                case '┴':
                    return '╩';
                case '╜':
                case '╛':
                case '┘':
                    return '╝';
                case '╝':
                case '╣':
                case '╬':
                default :
                    return c;
            }
        }
    }
    
    public static char addDoubleHorizontal(char c) {
        if (!isCharBoxBound(c)) {
            return '═';
        } else {
            switch (c) {
                case '│':
                    return '╪';
                case '┤':
                    return '╪';
                case '╡':
                    return '╪';
                case '╢':
                    return '╬';
                case '╖':
                    return '╦';
                case '╕':
                    return '╤';
                case '╣':
                    return '╬';
                case '║':
                    return '╬';
                case '╗':
                    return '╦';
                case '╝':
                    return '╩';
                case '╜':
                    return '╩';
                case '╛':
                    return '╩';
                case '┐':
                    return '╦';
                case '└':
                    return '╩';
                case '┴':
                    return '╧';
                case '┬':
                    return '╤';
                case '├':
                    return '┼';
                case '─':
                    return '═';
                case '┼':
                    return '╪';
                case '╞':
                    return '╬';
                case '╟':
                    return '╬';
                case '╚':
                    return '╩';
                case '╔':
                    return '╦';
                case '╩':
                    return c;
                case '╦':
                    return c;
                case '╠':
                    return '╬';
                case '═':
                    return c;
                case '╬':
                    return c;
                case '╧':
                    return '╧';
                case '╨':
                    return '╩';
                case '╤':
                    return '╤';
                case '╥':
                    return '╦';
                case '╙':
                    return '╩';
                case '╘':
                    return '╧';
                case '╒':
                    return '╤';
                case '╓':
                    return '╦';
                case '╫':
                    return '╬';
                case '╪':
                    return c;
                case '┘':
                    return '╧';
                case '┌':
                    return '╤';
                default :
                    return c;
            }
        }
    }
    public static char addDoubleVertical(char c) {
        if (!isCharBoxBound(c)) {
            return '║';
        } else {
            switch (c) {
                case '│':
                    return '║';
                case '┤':
                    return '╢';
                case '╡':
                    return '╣';
                case '╢':
                    return c;
                case '╖':
                    return '╢';
                case '╕':
                    return '╣';
                case '╣':
                    return c;
                case '║':
                    return c;
                case '╗':
                    return '╣';
                case '╝':
                    return '╣';
                case '╜':
                    return '╢';
                case '╛':
                    return '╣';
                case '┐':
                    return '╢';
                case '└':
                    return '╟';
                case '┴':
                    return '╫';
                case '┬':
                    return '╫';
                case '├':
                    return '╟';
                case '─':
                    return '╫';
                case '┼':
                    return '╫';
                case '╞':
                    return '╠';
                case '╟':
                    return c;
                case '╚':
                    return '╠';
                case '╔':
                    return '╠';
                case '╩':
                    return '╬';
                case '╦':
                    return '╬';
                case '╠':
                    return c;
                case '═':
                    return '╬';
                case '╬':
                    return c;
                case '╧':
                    return '╬';
                case '╨':
                    return '╫';
                case '╤':
                    return '╬';
                case '╥':
                    return '╫';
                case '╙':
                    return '╟';
                case '╓':
                    return '╟';
                case '╘':
                    return '╠';
                case '╒':
                    return '╠';
                case '╫':
                    return c;
                case '╪':
                    return '╬';
                case '┘':
                    return '╢';
                case '┌':
                    return '╟';
                default :
                    return c;
            }
        }
    }
    
}
