/*
 *     Copyright (C) 2016  Prateek Srivastava
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/gpl.html.
 *
 */

package com.srt.la4j.exceptions;

public class InCompatibleException extends Exception {
    public InCompatibleException() {
        super("Matrix is incompatible to perform the operation.");
    }

    public InCompatibleException(String msg){
        super(msg);
    }
}
