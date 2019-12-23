;;//TODO: FIXME
;;;;;;; DeOtp Start

::pdb::
Clipboard := ""
Run, <<PATH>>\getpdbotp.bat, <<PATH>>\pdbhack, Hide
ClipWait, 3
Send, ^v
return

;;;;;;; DeOtp End
