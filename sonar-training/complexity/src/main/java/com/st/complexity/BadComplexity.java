/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.complexity;

/**
 *
 * @author cstan
 */
public class BadComplexity {

    /**
     *
     * @param myCar.
     */
    public void process(final Car myCar) {
        if (myCar.isNotMine()) { /* 1*/
            if (myCar.isNotMine()) { /* 2*/
                if (myCar.isNotMine()) { /*3 */
                    if (myCar.isNotMine()) { /*4 */
                        if (myCar.isNotMine()) { /*5 */
                            if (myCar.isNotMine()) { /*6 */
                                if (myCar.isNotMine()) { /*7*/
                                    if (myCar.isNotMine()) { /*8*/
                                        if (myCar.isNotMine()) { /*9*/
                                            if (myCar.isNotMine()) { /*10*/

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
